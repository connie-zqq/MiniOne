package edu.northeastern.minione.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.rekognition.model.InvalidImageFormatException;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import edu.northeastern.minione.entity.AmazonImage;
import edu.northeastern.minione.entity.Moment;
import edu.northeastern.minione.entity.Space;
import edu.northeastern.minione.entity.User;
import edu.northeastern.minione.repository.AmazonImageRepository;
import edu.northeastern.minione.repository.SpaceRepository;
import edu.northeastern.minione.repository.UserRepository;
import edu.northeastern.minione.util.FileUploadUtil;

/**
 * This is the AmazonS3ImageService class, which used to send images to Amazon S3 Bucket.
 */
@Service
public class AmazonS3ImageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private AmazonImageRepository amazonImageRepository;

    @Autowired
    private AmazonClientService amazonClientService;

    /**
     * Upload the moment image(s) to S3 bucket.
     *
     * @param multipartFile an uploaded file received in a multipart request
     * @param moment        the Moment object
     * @return AmazonImage the AmazonImage object
     * @throws IOException if an I/O error occurs
     */
    public AmazonImage uploadMomentImageToS3(MultipartFile multipartFile, Moment moment) throws IOException {

        String objectKey = "";

        // if the extension of the file is not valid
        if (!FileUploadUtil.isValidImageFileExtension(multipartFile)) {
            throw new InvalidImageFormatException("Invalid Image Extension! Please Try Again!");
        }

        try {
            // the multipartFile has invalid extension
            // convert multipartFile to file
            File file = FileUploadUtil.convertMultipartToFile(multipartFile);

            // generate the file name (object key) to be uploaded to S3
            objectKey = FileUploadUtil.generateFileName(multipartFile);

            // upload file to S3
            uploadPublicFileToS3(objectKey, file);

            // delete the file
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save image information on MySQL server and return them
        AmazonImage amazonImage = new AmazonImage();
        amazonImage.setObjectKey(objectKey);
        amazonImage.setMoment(moment);

        return this.amazonImageRepository.save(amazonImage);
    }

    /**
     * Upload the user profile image to S3.
     *
     * @param multipartFile an uploaded file received in a multipart request
     * @param user          the User object
     * @throws IOException if an I/O error occurs
     */
    public void uploadUserProfileImageToS3(MultipartFile multipartFile, User user) throws IOException {

        String objectKey = "";

        // if the extension of the file is not valid
        if (!FileUploadUtil.isValidImageFileExtension(multipartFile)) {
            throw new InvalidImageFormatException("Invalid Image Extension! Please Try Again!");
        }

        try {
            // the multipartFile has invalid extension
            // convert multipartFile to file
            File file = FileUploadUtil.convertMultipartToFile(multipartFile);

            // generate the file name (object key) to be uploaded to S3
            objectKey = FileUploadUtil.generateFileName(multipartFile);

            // upload file to S3
            uploadPublicFileToS3(objectKey, file);

            // delete the file
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save image information on MySQL server and return them
        user.setUserImageObjectKey(objectKey);
        this.userRepository.save(user);
    }

    /**
     * Upload the baby profile image to S3.
     *
     * @param multipartFile an uploaded file received in a multipart request
     * @param space         the Space object
     * @throws IOException if an I/O error occurs
     */
    public void uploadBabyProfileImageToS3(MultipartFile multipartFile, Space space) throws IOException {

        String objectKey = "";

        // if the extension of the file is not valid
        if (!FileUploadUtil.isValidImageFileExtension(multipartFile)) {
            throw new InvalidImageFormatException("Invalid Image Extension! Please Try Again!");
        }

        try {
            // the multipartFile has invalid extension
            // convert multipartFile to file
            File file = FileUploadUtil.convertMultipartToFile(multipartFile);

            // generate the file name (object key) to be uploaded to S3
            objectKey = FileUploadUtil.generateFileName(multipartFile);

            // upload file to S3
            uploadPublicFileToS3(objectKey, file);

            // delete the file
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save image information on MySQL server and return them
        space.setBabyImageObjectKey(objectKey);
        this.spaceRepository.save(space);
    }

    /**
     * Upload a list of images to AWS S3 bucket.
     *
     * @param images the list of the images
     * @return the list of AmazonImage objects
     */
    public List<AmazonImage> uploadImages(List<MultipartFile> images, Moment moment) {
        List<AmazonImage> amazonImages = new ArrayList<>();
        images.forEach(image -> {
            try {
                amazonImages.add(uploadMomentImageToS3(image, moment));
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
     * @param amazonImage the AmazonImage object
     */
    public void deleteImageFromS3(AmazonImage amazonImage) {
        String ObjectKey = amazonImage.getObjectKey();
        amazonClientService.getS3client().deleteObject(new DeleteObjectRequest(amazonClientService.getBucketName(), ObjectKey));
        amazonImageRepository.delete(amazonImage);
    }

    /**
     * Upload public file to S3 bucket.
     *
     * @param fileName the fileName of the file to be uploaded
     * @param file     file
     */
    public void uploadPublicFileToS3(String fileName, File file) {
        amazonClientService.getS3client().putObject(new PutObjectRequest(amazonClientService.getBucketName(), fileName, file));
    }

    /**
     * Generate the pre-signed url of the file to be uploaded.
     *
     * @param objectKey The object key (or key name) uniquely identifies the object in an Amazon S3 bucket
     * @return the pre-signed url of the file to be uploaded
     */
    public URL getGeneratePresignedUrl(String objectKey) {
        if (objectKey == null) {
            return null;
        }

        // Set the pre-signed URL to expire after one hour.
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
