package gala.gala_api.controller;

import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;
import gala.gala_api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import gala.gala_api.entity.Event;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

  private EventService eventService;

  private AccountService accountService;

  @GetMapping("/users/{accountId}")
  public List<Event> retrieveUserEvents(@PathVariable("accountId") Long accountId, HttpServletResponse response) {
    Optional<Account> maybeAccount = accountService.findAccountById(accountId);

    if (maybeAccount.isPresent()) {
      Account account = maybeAccount.get();

      List<Event> events = eventService.retrieveEventsByAccount(account);

      if (events.size() > 0) {
        response.setStatus(HttpServletResponse.SC_OK);
        return events;
      } else {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      }
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.setHeader("gala-message", "Account with Id " + accountId.toString() + " was not found.");
    }

    return null;
  }

  @PostMapping("/users/{accountId}")
  public Event createNewUserEvent(@PathVariable("accountId") Long accountId, String name, String place, Date eventTime, int capacity, HttpServletResponse response) {
    Optional<Account> maybeAccount = accountService.findAccountById(accountId);

    if (maybeAccount.isPresent()) {
      Account account = maybeAccount.get();
      Event event = eventService.createEvent(account, name, place, eventTime, capacity);

      response.setStatus(HttpServletResponse.SC_OK);
      return event;
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.setHeader("gala-message", "Account with Id " + accountId.toString() + " was not found.");
    }
    return null;
  }

  @GetMapping("/events/{eventId}")
  public Event retrieveEventById(@PathVariable("eventId") String eventId, HttpServletResponse response) {
    Optional<Event> maybeEvent = eventService.findEvent(eventId);

    if (maybeEvent.isPresent()) {
      response.setStatus(HttpServletResponse.SC_OK);
      return maybeEvent.get();
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
    return null;
  }

  @Autowired
  public void setEventService(EventService eventService) {
    this.eventService = eventService;
  }

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }
}