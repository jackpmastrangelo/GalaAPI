package gala.gala_api.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AmazonClient {

  private AmazonS3 amazonClient;

  @Value("${amazonProperties.accessKey}")
  private String accessKey;

  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  private final Regions region = Regions.US_EAST_1;

  @PostConstruct
  private void initClient() {
    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
    this.amazonClient = AmazonS3ClientBuilder.standard().withCredentials(provider)
            .withRegion(region).build();
  }

  public AmazonS3 getAmazonClient() {
    return amazonClient;
  }

  public void setAmazonClient(AmazonS3 amazonClient) {
    this.amazonClient = amazonClient;
  }
}
