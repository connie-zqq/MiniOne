package edu.northeastern.minione.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.rekognition.model.InvalidImageFormatException;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import edu.northeastern.minione.model.AmazonImage;
import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.repository.AmazonImageRepository;
import edu.northeastern.minione.util.FileUploadUtil;

/**
 * This is a service to send images to Amazon S3 Bucket.
 *
 */
@Service
public class AmazonS3ImageService {

    @Autowired
    private AmazonImageRepository amazonImageRepository;

    @Autowired
    private AmazonClientService amazonClientService;

    public AmazonImage uploadImageToS3(MultipartFile multipartFile, Moment moment) throws IOException {

        String fileName = "";

        // create an array list of valid extensions
        List<String> validExtensions = Arrays.asList("png", "jpg", "jpeg");

        // get the extension of the multipartFile (using FilenameUtils of org.apache.commons.io)
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        // if the extension of the file is not valid
        if (!validExtensions.contains(extension)) {
            throw new InvalidImageFormatException("Invalid Image Extension! Please Try Again!");
        }

        try {
            // the multipartFile has invalid extension
            // convert multipartFile to file
            File file = FileUploadUtil.convertMultipartToFile(multipartFile);

            // generate the file name (object key) to be uploaded to S3
            fileName = FileUploadUtil.generateFileName(multipartFile);

            // upload file to S3
            uploadPublicFileToS3(fileName, file);

            // delete the file
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Save image information on MySQL server and return them
        AmazonImage amazonImage = new AmazonImage();
        amazonImage.setObjectKey(fileName);
        amazonImage.setMoment(moment);

        return this.amazonImageRepository.save(amazonImage);
    }

    /**
     * Upload a list of images to AWS S3 bucket.
     *
     * @param images
     * @return
     */
    public List<AmazonImage> uploadImages(List<MultipartFile> images, Moment moment) {
        List<AmazonImage> amazonImages = new ArrayList<>();
        images.forEach(image -> {
            try {
                amazonImages.add(uploadImageToS3(image, moment));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return amazonImages;
    }

    /**
     * Delete file from S3 Bucket.
     * S3 bucket cannot delete file by url. It requires a bucket name and a file name,
     * that’s why we retrieved file name from url.
     *
     * @param amazonImage
     */
    public void deleteImageFromS3(AmazonImage amazonImage) {
        String ObjectKey = amazonImage.getObjectKey();
        amazonClientService.getS3client().deleteObject(new DeleteObjectRequest(amazonClientService.getBucketName(), ObjectKey));
        amazonImageRepository.delete(amazonImage);
    }

    /**
     * Upload file to S3 bucket.
     *
     * @param fileName
     * @param file
     */
    public void uploadPublicFileToS3(String fileName, File file) {
        amazonClientService.getS3client().putObject(new PutObjectRequest(amazonClientService.getBucketName(), fileName, file));
    }

    /**
     * Generate the pre-signed url of the file to be uploaded.
     *
     * @param objectKey The object key (or key name) uniquely identifies the object in an Amazon S3 bucket.
     * @return
     */
    public URL getGeneratePresignedUrl(String objectKey) {
        // Set the presigned URL to expire after one hour.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        // Generate the pre-signed URL.
        System.out.println("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(amazonClientService.getBucketName(), objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        URL url = amazonClientService.getS3client().generatePresignedUrl(generatePresignedUrlRequest);

        System.out.println("Pre-Signed URL: " + url.toString());
        return url;
    }

}
