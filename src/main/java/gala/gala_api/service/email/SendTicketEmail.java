package gala.gala_api.service.email;

import gala.gala_api.service.AwsS3Service;

/**
 * This is a specific implementation of IEmail for when users are given their generated email.
 */
public class SendTicketEmail extends AbstractEmail {

  /**
   * Constructs a new SendTicketEmail.
   * @param eventName The name of the event that the ticket was generated for.
   */
  public SendTicketEmail(String eventName, String qrCodeNumber, AwsS3Service awsS3Service) {
    String subject = "Your ticket for " + eventName + " has arrived!";
    String textBody = "Your email does not support HTML currently, go to galatix.io to get your ticket.";

    String emailBody = awsS3Service.fetchS3ObjectAsString("gala-internal-filestore", "emails/sendEmailTemplate.html");

    emailBody = emailBody.replace("-EVENT_NAME-", eventName);
    emailBody = emailBody.replace("-QR_CODE_NUMBER-", qrCodeNumber);

    this.createEmailHelper("ticketing@galatix.io", subject, emailBody, textBody);
  }
}
