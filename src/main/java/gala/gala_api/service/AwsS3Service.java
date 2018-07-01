package gala.gala_api.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This service allows interaction with AWS S3 file storage.
 */
@Service
public class AwsS3Service {

  private AmazonS3 amazonClient;

  @Value("${amazonProperties.accessKey}")
  private String accessKey;

  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  private final Regions region = Regions.US_EAST_1;

  /**
   * Gets the given s3 file and returns its contents as a large string. Currently does not preserver newlines.
   *
   * @param bucket The AWS bucket where the file is stored.
   * @param key The actual key of the file within the bucket (This is essentially a filepath within the bucket.)s
   *
   * @return The s3 file's contents as a String.
   */
  public String getS3ObjectAsString(String bucket, String key) {
    AmazonS3 client = this.amazonClient;
    S3Object emailTemplateObj = client.getObject(bucket, key);
    BufferedReader s3Reader = new BufferedReader(new InputStreamReader(emailTemplateObj.getObjectContent()));

    StringBuilder result = new StringBuilder();
    String line;
    try {
      while ((line = s3Reader.readLine()) != null) {
        result.append(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return result.toString();
  }

  public void putS3ObjectFromByteArray(byte[] s3Obj, String bucket, String key) {
    AmazonS3 client = this.amazonClient;
    ByteArrayInputStream inputStream = new ByteArrayInputStream(s3Obj);
    PutObjectRequest request = new PutObjectRequest(bucket, key, inputStream, new ObjectMetadata());
    client.putObject(request);
  }

  @PostConstruct
  private void initClient() {
    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
    this.amazonClient = AmazonS3ClientBuilder.standard().withCredentials(provider)
            .withRegion(region).build();
  }

  //For testing purposes.
  public void setAmazonClient(AmazonS3 amazonClient) {
    this.amazonClient = amazonClient;
  }
}
