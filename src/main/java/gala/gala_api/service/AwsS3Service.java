package gala.gala_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This service allows interaction with AWS S3 file storage.
 */
@Service
public class AwsS3Service {

  //TODO: Extract bucket as property? Mainly if we use different buckets for different environments
  private static final String GALA_FILESTORE_BUCKET = "gala-internal-filestore";
  private static final String EMAIL_TEMPLATE = "emails/sendEmailTemplate.html";

  private AmazonS3 s3Client;

  /**
   * Gets the given s3 file and returns its contents as a large string. Currently does not preserver newlines.
   *
   * @param bucket The AWS bucket where the file is stored.
   * @param key The actual key of the file within the bucket (This is essentially a filepath within the bucket.)
   *
   * @return The s3 file's contents as a String.
   */
  public String getS3ObjectAsString(String bucket, String key) {
    S3Object emailTemplateObj = s3Client.getObject(bucket, key);
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

  public String fetchTicketEmailHtmlTemplate() {
    S3Object emailTemplate = s3Client.getObject(GALA_FILESTORE_BUCKET, EMAIL_TEMPLATE);
    return readToString(emailTemplate);
  }

  private String readToString(S3Object s3Object) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));

    StringBuilder result = new StringBuilder();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return result.toString();
  }

  public void putS3ObjectFromByteArray(byte[] s3Obj, String bucket, String key) {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(s3Obj);
    PutObjectRequest request = new PutObjectRequest(bucket, key, inputStream, new ObjectMetadata());
    s3Client.putObject(request);
  }

  @Autowired
  public void setS3Client(AmazonS3 s3Client) {
    this.s3Client = s3Client;
  }
}
