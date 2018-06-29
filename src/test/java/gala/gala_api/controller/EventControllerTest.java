package gala.gala_api.controller;

import gala.gala_api.entity.Account;
import gala.gala_api.entity.Event;
import gala.gala_api.service.AccountService;
import gala.gala_api.service.EventService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventControllerTest {

  @Test
  public void testRetrieveUserEvents() {
    EventController eventController = new EventController();
    EventService eventService = mock(EventService.class);

    Account account = new Account();
    account.setId("A1");
    Event event = new Event();
    Event event1 = new Event();

    TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(account, null);
    SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);

    HttpServletResponse httpServletResponse = new MockHttpServletResponse();

    when(eventService.retrieveEventsByAccount(account)).thenReturn(Arrays.asList(event, event1));
    eventController.setEventService(eventService);


    List<Event> eventList1 = eventController.retrieveUserEvents(httpServletResponse);

    assertEquals(HttpServletResponse.SC_OK, httpServletResponse.getStatus());
    assertEquals(2, eventList1.size());
  }

  @Test
  public void testCreateNewUserEvent() {
    EventController eventController = new EventController();
    EventService eventService = mock(EventService.class);

    Account account = new Account();
    Event event = new Event();
    HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    Date date = new Date();

    TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(account, null);
    SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);

    when(eventService.createEvent(account, "ACAIDA", "Acaida", date, 16))
            .thenReturn(event);

    eventController.setEventService(eventService);

    Event result = eventController.createNewUserEvent("ACAIDA", "Acaida",
            date, 16, httpServletResponse);

    assertEquals(event, result);
    assertEquals(HttpServletResponse.SC_OK, httpServletResponse.getStatus());
  }

  @Test
  public void testRetrieveEventById() {
    EventController eventController = new EventController();
    EventService eventService = mock(EventService.class);

    Event event = new Event();
    HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    HttpServletResponse httpServletResponse1 = new MockHttpServletResponse();

    when(eventService.findEvent("ID")).thenReturn(Optional.of(event));
    when(eventService.findEvent("ID1")).thenReturn(Optional.empty());

    eventController.setEventService(eventService);

    Event result = eventController.retrieveEventById("ID", httpServletResponse);
    Event result1 = eventController.retrieveEventById("ID1", httpServletResponse1);

    assertEquals(event, result);
    assertEquals(HttpServletResponse.SC_OK, httpServletResponse.getStatus());
    assertEquals(null, result1);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, httpServletResponse1.getStatus());
  }
}
