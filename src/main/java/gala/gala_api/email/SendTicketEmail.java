package gala.gala_api.email;

import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import gala.gala_api.service.AWSS3Service;
import java.io.IOException;

/**
 * This is a specific implementation of IEmail for when users are given their generated email.
 */
public class SendTicketEmail extends AbstractEmail {

  private String eventName;
  private AWSS3Service awss3Service;
  private String qrCodeNumber;

  /**
   * Constructs a new SendTicketEmail.
   * @param eventName The name of the event that the ticket was generated for.
   */
  public SendTicketEmail(String eventName, String qrCodeNumber) {
    this.eventName = eventName;
    this.awss3Service = new AWSS3Service();
    this.qrCodeNumber = qrCodeNumber;
  }

  /**
   * Creates the actual email request object that will be used to send this email.
   * @return A SendEmailRequest for this SendTicketEmail.
   */
  @Override
  public SendEmailRequest createEmailRequest() {
    String subject = "Your ticket for " + eventName + " has arrived!";

    String textBody = "Your email does not support HTML currently, go to galatix.io to get your ticket.";

    String emailBody;

    try {
      emailBody = awss3Service.getS3ObjectAsString("gala-internal-filestore", "emails/sendEmailTemplate.html");
    } catch (IOException e) {
      e.printStackTrace();
      emailBody = null;
      //TODO The error handling. This should fail loudly and notify that the user was never sent an email.
    }

    emailBody = emailBody.replace("-EVENT_NAME-", eventName);
    emailBody = emailBody.replace("-QR_CODE_NUMBER-", qrCodeNumber);

    return this.createEmailHelper("ticketing@galatix.io", subject, emailBody, textBody);
  }
}
