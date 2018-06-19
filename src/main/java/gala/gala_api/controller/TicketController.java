package gala.gala_api.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;
import gala.gala_api.service.EventService;
import gala.gala_api.service.TicketService;
import gala.gala_api.service.email.EmailService;
import gala.gala_api.service.email.SendTicketEmail;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tickets")
public class TicketController {

  @Autowired
  private TicketService ticketService;

  @Autowired
  private EventService eventService;

  @Autowired
  private EmailService emailService;

  @PostMapping
  public Ticket requestTicket(@RequestParam("event_id") String eventId,
                              @RequestParam("email") String email, HttpServletResponse response) {
    Optional<Event> maybeEvent = eventService.findEvent(eventId);

    if (maybeEvent.isPresent()) {
      Event event = maybeEvent.get();

      if (ticketService.areTicketsRemaining(event)) {
        Ticket ticket = ticketService.createTicket(event, email);

        ticketService.generateAndUploadQRCode(ticket.getId());
        emailService.sendEmail(email, new SendTicketEmail(event.getName(), ticket.getId()));

        response.setStatus(HttpServletResponse.SC_OK); //Success
        response.setHeader("gala-message","Ticket successfully added.");
        return ticket;
      } else {
        assignErrorStatusAndMessage(response, HttpStatus.SC_CONFLICT, "Event capacity "
                + "has already been reached.");
      }
    } else {
      assignErrorStatusAndMessage(response, HttpStatus.SC_NOT_FOUND, "Event with id "
              + eventId + " could not be found.");
    }

    return null;
  }

  @PutMapping("/validate")
  @ResponseStatus
  public void validateTicket(@RequestParam("ticketId") String ticketId, @RequestParam("eventId") String eventId, HttpServletResponse response) {
    Optional<Ticket> maybeTicket = ticketService.retrieveTicket(ticketId);
    if (maybeTicket.isPresent()) {
      Ticket ticket = maybeTicket.get();

      if (ticket.getEvent().getId().equals(eventId)) {
        switch (ticket.getStatus()) {
          case ACTIVE:
            ticketService.validateTicket(ticket);
            response.setStatus(HttpServletResponse.SC_OK);
            break;
          case VOIDED:
            assignErrorStatusAndMessage(response, HttpStatus.SC_NOT_ACCEPTABLE,"Ticket with Id "
                    + ticketId + " was voided. Could not validate.");
            break;
          case VALIDATED:
            assignErrorStatusAndMessage(response, HttpStatus.SC_NOT_ACCEPTABLE,"Ticket with Id "
                    + ticketId + " has already been validated. Could not validate.");
            break;
        }
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //TODO Right HTTP response?
      }
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void assignErrorStatusAndMessage(HttpServletResponse response, int statusCode, String errorMessage) {
    response.setStatus(statusCode);
    response.setHeader("gala-message", errorMessage);
  }
}