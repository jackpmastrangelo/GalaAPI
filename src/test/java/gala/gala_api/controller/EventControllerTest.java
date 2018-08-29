package gala.gala_api.controller;

import gala.gala_api.data_model.AccountUserDetails;
import gala.gala_api.entity.Account;
import gala.gala_api.entity.Event;
import gala.gala_api.service.EventService;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventControllerTest {

  private static final String EVENT_ID = "acadia_event";
  private static final String EVENT_NAME = "ACADIA";
  private static final String EVENT_PLACE = "Acadia";
  private static final Date EVENT_START_DATE = new Date();
  private static final Date EVENT_END_DATE = new Date();
  private static final int EVENT_CAPACITY = 10;
  private static final String EVENT_DESCRIPTION = "description";

  @Test
  public void testRetrieveUserEvents() {
    EventController eventController = new EventController();

    Account account = buildAccount();
    injectTestingAuthToken(account);

    EventService eventService = mock(EventService.class);
    when(eventService.retrieveEventsByAccount(account)).thenReturn(Arrays.asList(new Event(), new Event()));
    eventController.setEventService(eventService);

    List<Event> actualEvents = eventController.retrieveUserEvents();
    assertEquals(2, actualEvents.size());
  }

  @Test
  public void testCreateEvent() {
    EventController eventController = new EventController();

    Account account = buildAccount();
    injectTestingAuthToken(account);

    Event expectedEvent = buildEvent();
    EventService eventService = mock(EventService.class);
    when(eventService.createEvent(account, EVENT_NAME, EVENT_PLACE, EVENT_START_DATE,
            EVENT_END_DATE, EVENT_CAPACITY, EVENT_DESCRIPTION)).thenReturn(expectedEvent);
    eventController.setEventService(eventService);

    HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    Event actualEvent = eventController.createNewUserEvent(buildNewEventBody(), httpServletResponse);

    assertEquals(expectedEvent, actualEvent);
    assertEquals(HttpServletResponse.SC_OK, httpServletResponse.getStatus());
  }

  @Test
  public void testRetrieveEventById_EventExists() {
    EventController eventController = new EventController();

    Event expectedEvent = buildEvent();
    EventService eventService = mock(EventService.class);
    when(eventService.findEvent(EVENT_ID)).thenReturn(Optional.of(expectedEvent));
    eventController.setEventService(eventService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    Event actualEvent = eventController.retrieveEventById(EVENT_ID, mockResponse);

    assertEquals(expectedEvent, actualEvent);
    assertEquals(HttpServletResponse.SC_OK, mockResponse.getStatus());
  }

  @Test
  public void testRetrieveEventById_EventDoesNotExist() {
    EventController eventController = new EventController();

    String fakeEventId = "no_event_for_this_id";
    EventService mockEventService = mock(EventService.class);
    when(mockEventService.findEvent(fakeEventId)).thenReturn(Optional.empty());
    eventController.setEventService(mockEventService);

    HttpServletResponse mockResponse = new MockHttpServletResponse();
    Event actualEvent = eventController.retrieveEventById(fakeEventId, mockResponse);

    assertNull(actualEvent);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, mockResponse.getStatus());
  }

  private Account buildAccount() {
    Account account = new Account();
    account.setId("A1");
    account.setEmail("E");
    account.setPassword("P");

    return account;
  }

  private void injectTestingAuthToken(Account account) {
    AccountUserDetails accountUserDetails = new AccountUserDetails(account, null);

    TestingAuthenticationToken token = new TestingAuthenticationToken(accountUserDetails, null);
    SecurityContextHolder.getContext().setAuthentication(token);
  }

  private CreateNewUserEventBody buildNewEventBody() {
    CreateNewUserEventBody createNewUserEventBody = new CreateNewUserEventBody();
    createNewUserEventBody.setName(EVENT_NAME);
    createNewUserEventBody.setPlace(EVENT_PLACE);
    createNewUserEventBody.setStartTime(EVENT_START_DATE);
    createNewUserEventBody.setEndTime(EVENT_END_DATE);
    createNewUserEventBody.setCapacity(EVENT_CAPACITY);
    createNewUserEventBody.setDescription(EVENT_DESCRIPTION);

    return createNewUserEventBody;
  }

  private Event buildEvent() {
    Event event = new Event();
    event.setName(EVENT_NAME);
    event.setPlace(EVENT_PLACE);
    event.setStartTime(EVENT_START_DATE);
    event.setEndTime(EVENT_END_DATE);
    event.setCapacity(EVENT_CAPACITY);
    event.setDescription(EVENT_DESCRIPTION);

    return event;
  }
}
