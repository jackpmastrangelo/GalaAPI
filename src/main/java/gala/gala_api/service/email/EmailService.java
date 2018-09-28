package gala.gala_api.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gala.gala_api.entity.Ticket;

/**
 * This service exists to interface with AWS SES (Simple Email Service).
 */
@Service
public class EmailService {

  private EmailRequestFactoryService emailRequestFactoryService;
  private AmazonSimpleEmailService amazonEmailService;

  public void sendTicketEmail(Ticket ticket) {
    SendEmailRequest emailRequest = emailRequestFactoryService.buildTicketEmailRequest(ticket);
    amazonEmailService.sendEmail(emailRequest);
  }

  @Autowired
  public void setEmailRequestFactoryService(EmailRequestFactoryService emailRequestFactoryService) {
    this.emailRequestFactoryService = emailRequestFactoryService;
  }

  @Autowired
  public void setAmazonEmailService(AmazonSimpleEmailService amazonEmailService) {
    this.amazonEmailService = amazonEmailService;
  }
}
