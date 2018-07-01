package gala.gala_api.controller;

import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;
import gala.gala_api.entity.TicketStatus;
import gala.gala_api.service.AwsS3Service;
import gala.gala_api.service.EventService;
import gala.gala_api.service.TicketService;
import gala.gala_api.service.email.EmailService;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TicketControllerTest {

  @Test
  public void testRequestTicket() throws IOException {
    TicketController ticketController = new TicketController();
    TicketService ticketService = mock(TicketService.class);
    EventService eventService = mock(EventService.class);
    EmailService emailService = mock(EmailService.class);
    AwsS3Service awsS3Service = mock(AwsS3Service.class);

    Event event = new Event();
    event.setName("Ree");
    Event event1 = new Event();
    Ticket ticket = new Ticket();
    ticket.setId("TID");
    HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    HttpServletResponse httpServletResponse1 = new MockHttpServletResponse();
    HttpServletResponse httpServletResponse2 = new MockHttpServletResponse();

    when(eventService.findEvent("ID")).thenReturn(Optional.of(event));
    when(eventService.findEvent("ID1")).thenReturn(Optional.of(event1));
    when(eventService.findEvent("ID2")).thenReturn(Optional.empty());
    when(ticketService.areTicketsRemaining(event)).thenReturn(true);
    when(ticketService.areTicketsRemaining(event1)).thenReturn(false);
    when(ticketService.createTicket(event, "j@j.com")).thenReturn(ticket);
    when(awsS3Service.fetchS3ObjectAsString("gala-internal-filestore", "emails/sendEmailTemplate.html"))
            .thenReturn("-EVENT_NAME- || -QR_CODE_NUMBER- ");

    ticketController.setTicketService(ticketService);
    ticketController.setEventService(eventService);
    ticketController.setEmailService(emailService);
    ticketController.setAwsS3Service(awsS3Service);

    Ticket result = ticketController.requestTicket("ID", "j@j.com", httpServletResponse);
    Ticket result1 = ticketController.requestTicket("ID1", "j1@j.com", httpServletResponse1);
    Ticket result2 = ticketController.requestTicket("ID2", "j2@j.com", httpServletResponse2);

    assertEquals(ticket, result);
    assertEquals(HttpServletResponse.SC_OK, httpServletResponse.getStatus());
    assertEquals(null, result1);
    assertEquals(HttpServletResponse.SC_CONFLICT, httpServletResponse1.getStatus());
    assertEquals(null, result2);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, httpServletResponse2.getStatus());
  }

  @Test
  public void testValidateTicket() {
    TicketController ticketController = new TicketController();
    TicketService ticketService = mock(TicketService.class);
    ticketController.setTicketService(ticketService);

    //Tests successful scenario
    Ticket ticket = new Ticket();
    ticket.setStatus(TicketStatus.ACTIVE);
    Event event = new Event();
    event.setId("EID");
    ticket.setEvent(event);
    HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    when(ticketService.retrieveTicket("ID")).thenReturn(Optional.of(ticket));
    ticketController.validateTicket("ID", "EID", httpServletResponse);
    verify(ticketService, times(1)).validateTicket(ticket);
    assertEquals(HttpServletResponse.SC_OK, httpServletResponse.getStatus());

    //Tests scenario where validated
    Ticket ticket2 = new Ticket();
    ticket2.setStatus(TicketStatus.VALIDATED);
    ticket2.setEvent(event);
    HttpServletResponse httpServletResponse2 = new MockHttpServletResponse();
    when(ticketService.retrieveTicket("ID2")).thenReturn(Optional.of(ticket2));
    ticketController.validateTicket("ID2", "EID", httpServletResponse2);
    verify(ticketService, times(0)).validateTicket(ticket2);
    assertEquals(HttpServletResponse.SC_NOT_ACCEPTABLE, httpServletResponse2.getStatus());
    assertEquals("Ticket with Id ID2 has already been validated. Could not validate.",
            httpServletResponse2.getHeader("gala-message"));

    //Test where ticket could not be found
    HttpServletResponse httpServletResponse3 = new MockHttpServletResponse();
    when(ticketService.retrieveTicket("ID3")).thenReturn(Optional.empty());
    ticketController.validateTicket("ID3", "EID", httpServletResponse3);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, httpServletResponse3.getStatus());

    //Test where ticket belongs to wrong event
    HttpServletResponse httpServletResponse4 = new MockHttpServletResponse();
    Ticket ticket3 = new Ticket();
    ticket3.setStatus(TicketStatus.ACTIVE);
    ticket3.setEvent(event);
    when(ticketService.retrieveTicket("ID4")).thenReturn(Optional.of(ticket3));
    ticketController.validateTicket("ID4", "EID2", httpServletResponse4);
    assertEquals(HttpServletResponse.SC_CONFLICT, httpServletResponse4.getStatus());
  }
}
