package gala.gala_api.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import org.junit.Test;

import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailServiceTest {

  @Test
  public void testSendTicketEmail() {
    EmailService emailService = new EmailService();

    Ticket ticket = new Ticket();
    Event event = new Event();
    event.setName("some event");
    ticket.setEvent(event);
    ticket.setId("some ticket");

    SendEmailRequest expectedEmailRequest = new SendEmailRequest();
    expectedEmailRequest.setMessage(new Message().withBody(new Body().withText(new Content().withData("Wow"))));

    EmailRequestFactoryService mockFactoryService = mock(EmailRequestFactoryService.class);
    when(mockFactoryService.buildTicketEmailRequest(ticket)).thenReturn(expectedEmailRequest);
    emailService.setEmailRequestFactoryService(mockFactoryService);

    AmazonSimpleEmailService mockAmazonEmailService = mock(AmazonSimpleEmailService.class);
    emailService.setAmazonEmailService(mockAmazonEmailService);

    emailService.sendTicketEmail(ticket);

    verify(mockAmazonEmailService).sendEmail(expectedEmailRequest);
  }
}