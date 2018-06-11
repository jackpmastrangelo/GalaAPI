package gala.gala_api.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import gala.gala_api.email.IEmail;
import org.springframework.stereotype.Service;

/**
 * This service exists to interface with AWS SES (Simple Email Service).
 */
@Service
public class EmailService {

  private AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
          .standard().withRegion(Regions.US_EAST_1).build();


  /**
   * This class takes a single email address and an email and sends the email.
   *
   * @param toAddress The email address to send to.
   * @param email The IEmail of the actual email to be sent.
   */
  public void sendEmail(String toAddress, IEmail email) {
    SendEmailRequest request = email.createEmailRequest();
    request.withDestination(new Destination().withToAddresses(toAddress));

    try {
      client.sendEmail(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
