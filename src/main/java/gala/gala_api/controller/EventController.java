package gala.gala_api.controller;

import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;
import gala.gala_api.service.EventService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import gala.gala_api.entity.Event;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Controller for API endpoints relating to events,
 */
@RestController
@RequestMapping("/events")
public class EventController {

  private EventService eventService;

  private AccountService accountService;

  /**
   * This endpoint returns a JSONArray of all the events from the associated user. Authenticated user must be same as
   * user who's events are being returned.
   *
   * @param accountId The account of the user who's events are being requested.
   * @param response Response passed in by Spring.
   * @return The events if successful, otherwise return a not 200 status code and null. Refer to README API Spec.
   */
  @GetMapping("/users")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved user events."),
          @ApiResponse(code = 204, message = "User had no events."),
          @ApiResponse(code = 404, message = "User could not be found.")
  })
  public List<Event> retrieveUserEvents(@RequestParam("accountId") Long accountId, HttpServletResponse response) {
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

  //TODO validation on these properties resulting in different status code.
  /**
   * Creates a new event for the given account with the given parameters.
   *
   * @param accountId The account of the owner of the new event.
   * @param name The name of the event.
   * @param place String defining where the event will take place.
   * @param eventTime Datetime of the event.
   * @param capacity Maximum number of tickets that can be generated.
   * @param response Response object passed in by Spring
   *
   * @return The created Event if successful, otherwise different status codes. Refer to README API Spec.
   */
  @PostMapping("/users")
  @ApiResponses(value = {
          @ApiResponse(code=200, message = "Event successfully created"),
          @ApiResponse(code=404, message = "Account not found")
  })
  public Event createNewUserEvent(@RequestParam("accountId") Long accountId,
                                  @RequestParam("name") String name,
                                  @RequestParam("place") String place,
                                  @RequestParam("eventTime") @DateTimeFormat(pattern="MM-DD-YYYY") Date eventTime,
                                  @RequestParam("capacity") int capacity, HttpServletResponse response) {
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

  /**
   * Retrieves the event with the given Id.
   *
   * @param eventId The Id of the desired event.
   * @param response Response object passed in by Spring
   * @return The Event if found, otherwise different status codes. Refer to README API Spec.
   */
  @GetMapping("/{eventId}")
  @ApiResponses(value = {
          @ApiResponse(code=200, message = "Event found successfully."),
          @ApiResponse(code=404, message = "Event not found.")
  })
  public Event retrieveEventById(@RequestParam("eventId") String eventId, HttpServletResponse response) {
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