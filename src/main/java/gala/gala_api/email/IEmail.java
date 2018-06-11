package gala.gala_api.email;

import com.amazonaws.services.simpleemail.model.SendEmailRequest;

/**
 * This interface represents an Email that we want to send via AWS SES.
 */
public interface IEmail {

  /**
   * Creates an AWS SES email request that can be sent by the EmailService.
   *
   * @return A send email request to be sent.
   */
  SendEmailRequest createEmailRequest();
}
