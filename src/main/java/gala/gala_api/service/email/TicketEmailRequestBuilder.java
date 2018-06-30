package gala.gala_api.service.email;

import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gala.gala_api.entity.Ticket;
import gala.gala_api.service.AwsS3Service;

@Service
public class TicketEmailRequestBuilder extends AbstractEmailRequestBuilder {

  private AwsS3Service awsS3Service;

  public SendEmailRequest buildTicketEmailRequest(Ticket ticket) {
    String toAddress = ticket.getEmail();
    String subject = buildSubject(ticket.getEvent().getName());
    String htmlBody = buildHtmlBody(ticket.getEvent().getName(), ticket.getId());
    String textBody = buildTextBody();

    return this.buildEmailRequest(toAddress, subject, htmlBody, textBody);
  }

  private String buildSubject(String eventName) {
    return "Your ticket for " + eventName + " has arrived!";
  }

  private String buildHtmlBody(String eventName, String qrCodeNumber) {
    String htmlBody = awsS3Service.fetchTicketEmailHtmlTemplate();

    return htmlBody //TODO Close coupling, potentially pass to awsS3service
            .replace("-EVENT_NAME-", eventName)
            .replace("-QR_CODE_NUMBER-", qrCodeNumber);
  }

  private String buildTextBody() {
    return "Your email does not support HTML currently, go to galatix.io to get your ticket.";
  }

  @Autowired
  public void setAwsS3Service(AwsS3Service awsS3Service) {
    this.awsS3Service = awsS3Service;
  }
}
