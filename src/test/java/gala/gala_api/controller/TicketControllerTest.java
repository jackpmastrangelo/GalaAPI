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

  private static final String EVENT_ID = "eventid";
  private static final String TICKET_ID = "ticketid";
  private static final String EMAIL = "alice@example.com";

  @Test
  public void testRequestTicket_ValidEventWithTicketsRemaining() {
    Event expectedEvent = buildEvent(EVENT_ID);

    EventService mockEventService = mock(EventService.class);
    when(mockEventService.findEvent(EVENT_ID)).thenReturn(Optional.of(expectedEvent));

    Ticket expectedTicket = buildTicket(TICKET_ID, TicketStatus.ACTIVE, expectedEvent);
    TicketService mockTicketService = mock(TicketService.class);
    when(mockTicketService.areTicketsRemaining(expectedEvent)).thenReturn(true);
    when(mockTicketService.createTicket(expectedEvent, EMAIL)).thenReturn(expectedTicket);

    AwsS3Service mockAwsS3Service = mock(AwsS3Service.class);
    EmailService mockEmailService = mock(EmailService.class);

    HttpServletResponse mockResponse = new MockHttpServletResponse();

    TicketController controller = buildTicketController(mockEventService, mockTicketService,
            mockAwsS3Service, mockEmailService);
    Ticket actualTicket = controller.requestTicket(EVENT_ID, EMAIL, mockResponse);

    assertEquals(expectedTicket, actualTicket);
    assertEquals(HttpServletResponse.SC_OK, mockResponse.getStatus());

    verify(mockAwsS3Service).generateAndUploadQrCodeTicket(TICKET_ID);
    verify(mockEmailService).sendTicketEmail(expectedTicket);
  }

  @Test
  public void testRequestTicket_EventIsAtCapacity() {
    Event fullEvent = buildEvent(EVENT_ID);

    EventService mockEventService = mock(EventService.class);
    when(mockEventService.findEvent(EVENT_ID)).thenReturn(Optional.of(fullEvent));

    TicketService mockTicketService = mock(TicketService.class);
    when(mockTicketService.areTicketsRemaining(fullEvent)).thenReturn(false);

    HttpServletResponse mockResponse = new MockHttpServletResponse();

    TicketController controller = buildTicketController(mockEventService, mockTicketService);
    Ticket actualTicket = controller.requestTicket(EVENT_ID, EMAIL, mockResponse);

    assertEquals(null, actualTicket);
    assertEquals(HttpServletResponse.SC_CONFLICT, mockResponse.getStatus());
  }

  @Test
  public void testRequestTicket_NoEventForEventId() {
    TicketController controller = new TicketController();

    EventService mockEventService = mock(EventService.class);
    when(mockEventService.findEvent(EVENT_ID)).thenReturn(Optional.empty());
    controller.setEventService(mockEventService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();

    Ticket actualTicket = controller.requestTicket(EVENT_ID, EMAIL, mockResponse);

    assertEquals(null, actualTicket);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, mockResponse.getStatus());
  }

  @Test
  public void testValidateTicket_Success() {
    TicketController controller = new TicketController();

    Event validEvent = buildEvent(EVENT_ID);
    Ticket ticketForEvent = buildTicket(TICKET_ID, TicketStatus.ACTIVE, validEvent);

    TicketService mockTicketService = mock(TicketService.class);
    when(mockTicketService.retrieveTicket(TICKET_ID)).thenReturn(Optional.of(ticketForEvent));
    controller.setTicketService(mockTicketService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.validateTicket(TICKET_ID, EVENT_ID, mockResponse);

    verify(mockTicketService).validateTicket(ticketForEvent);
    assertEquals(HttpServletResponse.SC_OK, mockResponse.getStatus());
  }

  @Test
  public void testValidateTicket_TicketIsAlreadyValidated() {
    TicketController controller = new TicketController();

    Event validEvent = buildEvent(EVENT_ID);
    Ticket ticketForEvent = buildTicket(TICKET_ID, TicketStatus.VALIDATED, validEvent);

    TicketService mockTicketService = mock(TicketService.class);
    when(mockTicketService.retrieveTicket(TICKET_ID)).thenReturn(Optional.of(ticketForEvent));
    controller.setTicketService(mockTicketService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.validateTicket(TICKET_ID, EVENT_ID, mockResponse);

    verify(mockTicketService, never()).validateTicket(ticketForEvent);
    assertEquals(HttpServletResponse.SC_NOT_ACCEPTABLE, mockResponse.getStatus());
  }

  @Test
  public void testValidateTicket_TicketIsForDifferentEvent() {
    TicketController controller = new TicketController();

    Event differentEvent = buildEvent("different eventid");
    Ticket ticketForDifferentEvent = buildTicket(TICKET_ID, TicketStatus.ACTIVE, differentEvent);

    TicketService mockTicketService = mock(TicketService.class);
    when(mockTicketService.retrieveTicket(TICKET_ID)).thenReturn(Optional.of(ticketForDifferentEvent));
    controller.setTicketService(mockTicketService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.validateTicket(TICKET_ID, EVENT_ID, mockResponse);

    verify(mockTicketService, never()).validateTicket(ticketForDifferentEvent);
    assertEquals(HttpServletResponse.SC_CONFLICT, mockResponse.getStatus());
  }

  @Test
  public void testValidateTicket_TicketNotFound() {
    TicketController controller = new TicketController();

    TicketService mockTicketService = mock(TicketService.class);
    when(mockTicketService.retrieveTicket(TICKET_ID)).thenReturn(Optional.empty());
    controller.setTicketService(mockTicketService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    controller.validateTicket(TICKET_ID, EVENT_ID, mockResponse);

    assertEquals(HttpServletResponse.SC_NOT_FOUND, mockResponse.getStatus());
  }

  private Event buildEvent(String eventId) {
    Event event = new Event();
    event.setId(eventId);

    return event;
  }

  private Ticket buildTicket(String ticketId, TicketStatus status, Event event) {
    Ticket ticket = new Ticket();
    ticket.setId(ticketId);
    ticket.setStatus(status);
    ticket.setEvent(event);

    return ticket;
  }

  private TicketController buildTicketController(EventService eventService, TicketService ticketService) {
    return buildTicketController(eventService, ticketService, null, null);
  }

  private TicketController buildTicketController(EventService eventService, TicketService ticketService,
                                                 AwsS3Service awsS3Service, EmailService emailService) {
    TicketController controller = new TicketController();
    controller.setEventService(eventService);
    controller.setTicketService(ticketService);
    controller.setAwsS3Service(awsS3Service);
    controller.setEmailService(emailService);

    return controller;
  }
}
