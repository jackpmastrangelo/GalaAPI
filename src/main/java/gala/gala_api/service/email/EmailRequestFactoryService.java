package gala.gala_api.service.email;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gala.gala_api.entity.Ticket;
import gala.gala_api.service.AwsS3Service;

@Service
public class EmailRequestFactoryService {

  //TODO property?
  private static final String TICKETING_GALATIX_EMAIL = "ticketing@galatix.io";
  private static final String UTF_8 = "UTF-8";

  private AwsS3Service awsS3Service;

  public SendEmailRequest buildTicketEmailRequest(Ticket ticket) {
    String eventName = ticket.getEvent().getName();

    String toAddress = ticket.getEmail();
    String subject = "Your ticket for " + eventName + " has arrived!";
    String bodyAsHtml = awsS3Service.fetchTicketEmailHtml(eventName, ticket.getId());
    String bodyAsText = "Your email does not support HTML currently, go to galatix.io to get your ticket.";

    return this.buildEmailRequest(toAddress, subject, bodyAsHtml, bodyAsText);
  }

  private SendEmailRequest buildEmailRequest(String toAddress, String subject, String htmlBody, String textBody) {
    Content emailSubject = new Content().withCharset(UTF_8).withData(subject);

    Content emailBodyHtml = new Content().withCharset(UTF_8).withData(htmlBody);
    Content emailBodyText = new Content().withCharset(UTF_8).withData(textBody);
    Body emailBody = new Body().withHtml(emailBodyHtml).withText(emailBodyText);

    Message fullEmailMessage = new Message().withSubject(emailSubject).withBody(emailBody);

    return new SendEmailRequest()
            .withSource(TICKETING_GALATIX_EMAIL)
            .withDestination(new Destination().withToAddresses(toAddress))
            .withMessage(fullEmailMessage);
  }

  @Autowired
  public void setAwsS3Service(AwsS3Service awsS3Service) {
    this.awsS3Service = awsS3Service;
  }
}
