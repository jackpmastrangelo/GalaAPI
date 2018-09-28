package gala.gala_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.StringInputStream;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsS3ServiceTest {

  private final String GALA_FILESTORE_BUCKET = "gala-internal-filestore";
  private final String TICKET_EMAIL_KEY = "emails/sendEmailTemplate.html";

  @Test
  public void testFetchTicketEmailHtml() throws UnsupportedEncodingException {
    AwsS3Service awsS3Service = new AwsS3Service();

    final String EMAIL_TEMPLATE = "event: -EVENT_NAME-, ticketid: -QR_CODE_NUMBER-";
    AmazonS3 mockAmazonS3 = mock(AmazonS3.class);
    S3Object expectedTemplate = new S3Object();
    expectedTemplate.setObjectContent(new StringInputStream(EMAIL_TEMPLATE));
    when(mockAmazonS3.getObject(GALA_FILESTORE_BUCKET, TICKET_EMAIL_KEY)).thenReturn(expectedTemplate);
    awsS3Service.setAmazonS3Client(mockAmazonS3);

    final String EVENT_NAME = "eventName";
    final String TICKET_ID = "a1b2c3d4";
    String actualEmailHtml = awsS3Service.fetchTicketEmailHtml(EVENT_NAME, TICKET_ID);

    assertEquals("event: " + EVENT_NAME + ", ticketid: " + TICKET_ID, actualEmailHtml);
  }
}