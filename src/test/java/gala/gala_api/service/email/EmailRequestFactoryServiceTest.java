package gala.gala_api.service.email;

import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import org.junit.Test;

import java.util.List;

import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;
import gala.gala_api.service.AwsS3Service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmailRequestFactoryServiceTest {

  private static final String TICKET_ID = "ticket123";
  private static final String EMAIL = "alice@example.com";
  private static final String EVENT_NAME = "Loftman Gala!";

  private static final String TICKETING_GALATIX_EMAIL = "ticketing@galatix.io";

  private static final String FAKE_HTML_BODY = "<fake>HTML</fake>";

  @Test
  public void testBuildTicketEmailRequest() {
    Event event = new Event();
    event.setName(EVENT_NAME);

    Ticket ticket = new Ticket();
    ticket.setId(TICKET_ID);
    ticket.setEvent(event);
    ticket.setEmail(EMAIL);

    EmailRequestFactoryService factory = new EmailRequestFactoryService();
    AwsS3Service mockAwsService = mock(AwsS3Service.class);
    when(mockAwsService.fetchTicketEmailHtml(EVENT_NAME, TICKET_ID)).thenReturn(FAKE_HTML_BODY);
    factory.setAwsS3Service(mockAwsService);

    SendEmailRequest actualEmailRequest = factory.buildTicketEmailRequest(ticket);

    String actualSourceEmail = actualEmailRequest.getSource();
    List<String> actualRecipientEmails = actualEmailRequest.getDestination().getToAddresses();
    String actualSubject = actualEmailRequest.getMessage().getSubject().getData();
    String actualHtmlBody = actualEmailRequest.getMessage().getBody().getHtml().getData();
    String actualTextBody = actualEmailRequest.getMessage().getBody().getText().getData();

    assertEquals(TICKETING_GALATIX_EMAIL, actualSourceEmail);

    assertEquals(1, actualRecipientEmails.size());
    assertEquals(EMAIL, actualRecipientEmails.get(0));

    assertEquals("Your ticket for " + EVENT_NAME + " has arrived!", actualSubject);
    assertEquals(FAKE_HTML_BODY, actualHtmlBody);
    assertEquals("Your email does not support HTML currently, go to galatix.io to get your ticket.",
            actualTextBody);
  }

}