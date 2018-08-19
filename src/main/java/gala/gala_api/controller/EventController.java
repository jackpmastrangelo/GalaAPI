package gala.gala_api.controller;

import gala.gala_api.data_model.AccountUserDetails;
import gala.gala_api.entity.Account;
import gala.gala_api.service.AccountService;
import gala.gala_api.service.EventService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
   * @param response Http response passed in by Spring.
   *
   * @return The events associated with this user.
   */
  @GetMapping("/users")
  @ApiResponses(value = {
          @ApiResponse(code = HttpStatus.SC_OK, message = "Successfully retrieved user events."),
  })
  public List<Event> retrieveUserEvents(HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account account = ((AccountUserDetails) authentication.getPrincipal()).getAccount();

    return eventService.retrieveEventsByAccount(account);
  }

  /**
   * Creates a new event for the given account with the given parameters.
   *
   * @param body Details of new event.
   * @param response Response object passed in by Spring
   *
   * @return The created Event if successful, otherwise different status codes. Refer to README API Spec.
   */
  @PostMapping("/users")
  @ApiResponses(value = {
          @ApiResponse(code=HttpStatus.SC_OK, message = "Event successfully created")
  })
  public Event createNewUserEvent(@RequestBody CreateNewUserEventBody body, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account account = ((AccountUserDetails) authentication.getPrincipal()).getAccount();
    Event event = eventService.createEvent(account, body.getName(), body.getPlace(), body.getStartTime(),
            body.getEndTime(), body.getCapacity(), body.getDescription());

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

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }
}

/**
 * name The name of the event.
 *  place String defining where the event will take place.
 *    * @param startTime Datetime of the event.
 *    * @param capacity Maximum number of tickets that can be generated.
 *    * @param description A description of the event.
 */
class CreateNewUserEventBody {
  //The name of the event.
  private String name;

  //String defining where the event will take place.
  private String place;

  //TODO How pass in dates well?
  //Start datetime of the event.
  private Date startTime;

  //End datetime of the event.
  private Date endTime;

  //Maximum number of tickets that can be generated.
  private int capacity;

  //A description of the event.
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}