package gala.gala_api.service.email;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
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
   * @param request The AWS SendEmailRequest object for the email to be sent.
   */
  public void sendEmail(String toAddress, SendEmailRequest request) {
    request.withDestination(new Destination().withToAddresses(toAddress));

    try {
      client.sendEmail(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
