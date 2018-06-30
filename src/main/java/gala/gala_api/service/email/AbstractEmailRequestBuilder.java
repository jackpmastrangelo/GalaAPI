package gala.gala_api.service.email;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

abstract class AbstractEmailRequestBuilder {

  //TODO Pull into application property?
  private static final String GALATIX_EMAIL = "ticketing@galatix.io";

  private static final String UTF_8 = "UTF-8";

  SendEmailRequest buildEmailRequest(String toAddress, String subject, String htmlBody, String textBody) {
    Content emailSubject = new Content().withCharset(UTF_8).withData(subject);

    Content emailBodyHtml = new Content().withCharset(UTF_8).withData(htmlBody);
    Content emailBodyText = new Content().withCharset(UTF_8).withData(textBody);
    Body emailBody = new Body().withHtml(emailBodyHtml).withText(emailBodyText);

    Message fullEmailMessage = new Message().withSubject(emailSubject).withBody(emailBody);

    return new SendEmailRequest()
            .withSource(GALATIX_EMAIL)
            .withDestination(new Destination().withToAddresses(toAddress))
            .withMessage(fullEmailMessage);
  }
}
