package gala.gala_api.controller;

import gala.gala_api.data_model.AccountUserDetails;
import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;
import gala.gala_api.service.EventService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import gala.gala_api.entity.Event;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Controller for API endpoints relating to events.
 */
@RestController
@RequestMapping("/events")
public class EventController {

  private EventService eventService;

  /**
   * This endpoint returns a JSONArray of all the events from the associated user. Authenticated user must be same as
   * user who's events are being returned.
   *
   * @return The events associated with this user.
   */
  @GetMapping("/users")
  @ApiResponses(value = {
          @ApiResponse(code = HttpStatus.SC_OK, message = "Successfully retrieved user events."),
  })
  public List<Event> retrieveUserEvents() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account account = ((AccountUserDetails) authentication.getPrincipal()).getAccount();

    return eventService.retrieveEventsByAccount(account);
  }

  /**
   * Creates a new event for the given account with the given parameters.
   *
   * @param name The name of the event.
   * @param place String defining where the event will take place.
   * @param eventTime Datetime of the event.
   * @param capacity Maximum number of tickets that can be generated.
   *
   * @return The created Event if successful, otherwise different status codes. Refer to README API Spec.
   */
  @PostMapping("/users")
  @ApiResponses(value = {
          @ApiResponse(code=HttpStatus.SC_OK, message = "Event successfully created.")
  })
  public Event createEvent(@RequestParam("name") String name,
                           @RequestParam("place") String place,
                           //TODO How pass in dates well?
                           @RequestParam("eventTime") @DateTimeFormat(pattern="MM-DD-YYYY") Date eventTime,
                           @RequestParam("capacity") int capacity) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account account = ((AccountUserDetails) authentication.getPrincipal()).getAccount();
    Event event = eventService.createEvent(account, name, place, eventTime, capacity);

    return event;
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
          @ApiResponse(code=HttpStatus.SC_OK, message = "Event found successfully."),
          @ApiResponse(code=HttpStatus.SC_NOT_FOUND, message = "Event not found.")
  })
  public Event retrieveEventById(@PathVariable("eventId") String eventId, HttpServletResponse response) {
    Optional<Event> maybeEvent = eventService.findEvent(eventId);
    if (maybeEvent.isPresent()) {
      return maybeEvent.get();
    }

    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }

  @Autowired
  public void setEventService(EventService eventService) {
    this.eventService = eventService;
  }
}