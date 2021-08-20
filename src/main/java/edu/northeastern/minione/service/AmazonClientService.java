package edu.northeastern.minione.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


/**
 * This is the amazonClientService class.
 * To make requests to AWS, we need to first create a service client object.
 * <p>
 * Ref: https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/creating-clients.html
 */
@Service
public class AmazonClientService {

    // AmazonS3 is a class from amazon dependency
    private AmazonS3 s3client;

    // S3 bucket Url example: https://{bucket-name}.s3-{region}.amazonaws.com/
    // The fields below are a representation of variables from application.properties file
    // The @Value annotation will bind application properties directly to class files during
    // application initialization
    @Value("${amazon.s3.endpoint}")
    private String endpointUrl;

    // Bucket name
    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    // The IAM access key
    @Value("${amazon.s3.access-key}")
    private String accessKey;

    // The IAM secret key.
    @Value("${amazon.s3.secret-key}")
    private String secretKey;

    public AmazonS3 getS3client() {
        return s3client;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public String getBucketName() {
        return bucketName;
    }

    /**
     * Set Amazon credentials to Amazon client.
     * <p>
     * Notice: Annotation @PostConstruct is needed to run this method after constructor will be called, because
     * class fields marked with @Value annotation is null in the constructor.
     */
    @PostConstruct
    public void initializeAmazon() {

        // Init your AmazonS3 credentials using BasicAWSCredentials
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        // Start the client using AmazonS3ClientBuilder
        this.s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }
}
