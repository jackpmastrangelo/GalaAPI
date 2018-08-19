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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventControllerTest {

  @Test
  public void testRetrieveUserEvents() {
    EventController eventController = new EventController();
    EventService eventService = mock(EventService.class);

    List<GrantedAuthority> ROLES_FOR_ALL_USERS = AuthorityUtils.createAuthorityList("USER");
    Account account = new Account();
    account.setId("A1");
    account.setEmail("E");
    account.setPassword("P");
    AccountUserDetails accountUserDetails = new AccountUserDetails(account, ROLES_FOR_ALL_USERS);

    Event event = new Event();
    Event event1 = new Event();

    TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(accountUserDetails, null);
    SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);

    HttpServletResponse httpServletResponse = new MockHttpServletResponse();

    when(eventService.retrieveEventsByAccount(account)).thenReturn(Arrays.asList(event, event1));
    eventController.setEventService(eventService);


    List<Event> eventList1 = eventController.retrieveUserEvents();

    assertEquals(HttpServletResponse.SC_OK, httpServletResponse.getStatus());
    assertEquals(2, eventList1.size());
  }

  @Test
  public void testCreateEvent() {
    EventController eventController = new EventController();
    EventService eventService = mock(EventService.class);

    List<GrantedAuthority> ROLES_FOR_ALL_USERS = AuthorityUtils.createAuthorityList("USER");
    Account account = new Account();
    account.setId("A1");
    account.setEmail("E");
    account.setPassword("P");
    AccountUserDetails accountUserDetails = new AccountUserDetails(account, ROLES_FOR_ALL_USERS);

    Event event = new Event();
    HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    Date date = new Date();

    TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(accountUserDetails, null);
    SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);

    when(eventService.createEvent(account, "ACADIA", "Acadia", date, date,16, "DESC"))
            .thenReturn(event);

    eventController.setEventService(eventService);

    CreateNewUserEventBody createNewUserEventBody = new CreateNewUserEventBody();
    createNewUserEventBody.setName("ACADIA");
    createNewUserEventBody.setPlace("Acadia");
    createNewUserEventBody.setStartTime(date);
    createNewUserEventBody.setEndTime(date);
    createNewUserEventBody.setCapacity(16);
    createNewUserEventBody.setDescription("DESC");

    Event result = eventController.createNewUserEvent(createNewUserEventBody, httpServletResponse);

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
