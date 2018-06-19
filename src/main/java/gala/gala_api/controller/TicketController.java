package gala.gala_api.controller;

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

  @PostMapping("/create")
  public Ticket requestTicket(@RequestParam("event_id") String eventId, @RequestParam("email") String email, HttpServletResponse response) {
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
        response.setStatus(HttpServletResponse.SC_CONFLICT); //Conflict
        response.setHeader("gala-message","Event capacity has already been reached.");
      }
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND); //Not found.
      response.setHeader("gala-message", "Event with id " + eventId + " could not be found.");
    }

    return null;
  }

  @PutMapping("/validate")
  @ResponseStatus
  public void validateTicket(@RequestParam("ticket_id") String ticketId, @RequestParam("event_id") String eventId, HttpServletResponse response) {
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
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);//Not acceptable (harsh)
            response.setHeader("gala-message","Ticket with Id " + ticketId + " was voided. Could not validate.");
            break;
          case VALIDATED:
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.setHeader("gala-message", "Ticket with Id " + ticketId + " has already been validated. Could not validate.");
            break;
        }
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //TODO Right HTTP response?
      }
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}