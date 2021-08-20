package edu.northeastern.minione.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    /**
     * Convert MultipartFile-form data provided by Spring to File (java.io.File).
     * S3 bucket uploading method requires File as a parameter.
     *
     * @param file the file
     * @return the file
     * @throws IOException if an I/O error occurs
     */

    public static File convertMultipartToFile(MultipartFile file) throws IOException {

        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        // Creates a file output stream to write to the file represented by the specified File object.
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        return convertedFile;
    }

    /**
     * Generate the unique file name, that also known as "object key". Use a timestamp and replace all spaces in filename
     * with underscores to avoid repetition.
     *
     * @param multipartFile an uploaded file received in a multipart request
     * @return the generated file name
     */
    public static String generateFileName(MultipartFile multipartFile) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multipartFile.getOriginalFilename())
                .replace(" ", "_");
    }

    /**
     * Check if the multipartFile has the valid image file extension.
     *
     * @param multipartFile an uploaded file received in a multipart request
     * @return true if the multipartFile has valid image file extension, otherwise false
     */
    public static Boolean isValidImageFileExtension(MultipartFile multipartFile) {

        // create an array list of valid extensions
        List<String> validExtensions = Arrays.asList("png", "jpg", "jpeg");

        // get the extension of the multipartFile (using FilenameUtils of org.apache.commons.io)
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        // if the extension of the file is not valid
        return validExtensions.contains(extension);
    }

}
