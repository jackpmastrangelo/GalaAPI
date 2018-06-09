package gala.gala_api.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This service allows interaction with AWS S3 file storage.
 */
@Service
public class AWSS3Service {

  /**
   * Gets the given s3 file and returns its contents as a large string. Currently does not preserver newlines.
   *
   * @param bucket The AWS bucket where the file is stored.
   * @param key The actual key of the file within the bucket (This is essentially a filepath within the bucket.)
   *
   * @return The s3 file's contents as a String.
   * @throws IOException If there is an issue reading the file.
   */
  public String getS3ObjectAsString(String bucket, String key) throws IOException {
    S3Object emailTemplateObj = AmazonS3Client.builder().withRegion(Regions.US_EAST_1).build()
            .getObject(bucket, key);

    BufferedReader s3Reader = new BufferedReader(new InputStreamReader(emailTemplateObj.getObjectContent()));

    StringBuilder result = new StringBuilder();

    String line;

    while ((line = s3Reader.readLine()) != null) {
      result.append(line);
    }

    return result.toString();
  }

}
