package gala.gala_api.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

  @Value("${amazonProperties.accessKey}")
  private String accessKey;

  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  final private Regions awsRegion = Regions.US_EAST_1;

  @Bean
  public AWSCredentialsProvider awsCredentialsProvider() {
    return new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey));
  }

  @Bean
  public AmazonS3 amazonS3() {
    return AmazonS3Client.builder().withCredentials(this.awsCredentialsProvider()).withRegion(awsRegion).build();
  }

  @Bean
  public AmazonSimpleEmailService amazonSimpleEmailService() {
    return AmazonSimpleEmailServiceClientBuilder.standard().withRegion(awsRegion)
            .withCredentials(this.awsCredentialsProvider()).build();
  }
}
