package gala.gala_api.email;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

/**
 * This abstract class provides a helper method for creating an AWS SES email.
 */
abstract class AbstractEmail implements IEmail {

  /**
   * Creates a SendEmailRequest for AWS SES with the given details.
   *
   * @param fromEmail The email we want to send from, must be a valid email on Gala's SES.
   * @param subject The subject line of the email.
   * @param htmlBody The body of the email in HTML, this is the typical email.
   * @param textBody Text for the email if someone has a text-only browser.
   *
   * @return A SendEmailRequest with a setup email that can be sent by EmailService.
   */
  protected SendEmailRequest createEmailHelper(String fromEmail, String subject, String htmlBody, String textBody) {
    Content textContent = new Content().withCharset("UTF-8").withData(textBody);

    Content htmlContent = new Content().withCharset("UTF-8").withData(htmlBody);

    Body body = new Body().withHtml(htmlContent).withText(textContent);

    Content subjectContent = new Content().withCharset("UTF-8").withData(subject);

    Message message = new Message().withBody(body).withSubject(subjectContent);

    return new SendEmailRequest().withMessage(message).withSource(fromEmail);
  }
}
