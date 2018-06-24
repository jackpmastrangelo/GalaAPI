package gala.gala_api.controller;

import gala.gala_api.entity.Account;
import gala.gala_api.entity.Event;
import gala.gala_api.service.AccountService;
import gala.gala_api.service.EventService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

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
    AccountService accountService = mock(AccountService.class);
    EventService eventService = mock(EventService.class);

    Account account1 = new Account();
    Account account2 = new Account();
    Event event1 = new Event();
    Event event2 = new Event();

    HttpServletResponse httpServletResponse1 = new MockHttpServletResponse();
    HttpServletResponse httpServletResponse2 = new MockHttpServletResponse();
    HttpServletResponse httpServletResponse3 = new MockHttpServletResponse();

    when(accountService.findAccountById(1L)).thenReturn(Optional.of(account1));
    when(accountService.findAccountById(2L)).thenReturn(Optional.of(account2));
    when(accountService.findAccountById(3L)).thenReturn(Optional.empty());
    when(eventService.retrieveEventsByAccount(account1)).thenReturn(Arrays.asList(event1, event2));
    when(eventService.retrieveEventsByAccount(account2)).thenReturn(new ArrayList<>());

    eventController.setAccountService(accountService);
    eventController.setEventService(eventService);

    List<Event> eventList1 = eventController.retrieveUserEvents(1L, httpServletResponse1);
    List<Event> eventList2 = eventController.retrieveUserEvents(2L, httpServletResponse2);
    List<Event> eventList3 = eventController.retrieveUserEvents(3L, httpServletResponse3);

    assertEquals(HttpServletResponse.SC_OK, httpServletResponse1.getStatus());
    assertEquals(2, eventList1.size());
    assertEquals(HttpServletResponse.SC_NO_CONTENT, httpServletResponse2.getStatus());
    assertEquals(null, eventList2);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, httpServletResponse3.getStatus());
    assertEquals(null, eventList3);
  }

  @Test
  public void testCreateNewUserEvent() {
    EventController eventController = new EventController();
    AccountService accountService = mock(AccountService.class);
    EventService eventService = mock(EventService.class);

    Account account = new Account();
    Event event = new Event();
    HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    HttpServletResponse httpServletResponse1 = new MockHttpServletResponse();
    Date date = new Date();


    when(accountService.findAccountById(1L)).thenReturn(Optional.of(account));
    when(accountService.findAccountById(2L)).thenReturn(Optional.empty());
    when(eventService.createEvent(account, "ACAIDA", "Acaida", date, 16))
            .thenReturn(event);

    eventController.setAccountService(accountService);
    eventController.setEventService(eventService);

    Event result = eventController.createNewUserEvent(1L, "ACAIDA", "Acaida",
            date, 16, httpServletResponse);
    Event result1 = eventController.createNewUserEvent(2L, "ACAIDA", "Acaida",
            date, 16, httpServletResponse1);

    assertEquals(event, result);
    assertEquals(HttpServletResponse.SC_OK, httpServletResponse.getStatus());
    assertEquals(null, result1);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, httpServletResponse1.getStatus());
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
