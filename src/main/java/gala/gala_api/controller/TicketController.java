package gala.gala_api.controller;

import gala.gala_api.service.AwsS3Service;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;
import gala.gala_api.service.EventService;
import gala.gala_api.service.TicketService;
import gala.gala_api.service.email.EmailService;

import javax.servlet.http.HttpServletResponse;

/**
 * Controller for API endpoints relating to tickets.
 */
@RestController
@RequestMapping("/tickets")
public class TicketController {

  private TicketService ticketService;
  private EventService eventService;
  private AwsS3Service awsS3Service;
  private EmailService emailService;

  /**
   * Creates a ticket for the given event, generates a QR Code with the given ticketId, and sends
   * an email with the QR code.
   *
   * @param eventId Id of the event to create a ticket for.
   * @param email Email to send the QR code to.
   * @param response Response passed in by Spring.
   * @return The ticket that was created.
   */
  @PostMapping
  @ApiResponses(value = {
          @ApiResponse(code=HttpStatus.SC_OK, message = "Ticket successfully added."),
          @ApiResponse(code=HttpStatus.SC_NOT_FOUND, message = "Event not found"),
          @ApiResponse(code=HttpStatus.SC_CONFLICT, message = "Event capacity has already been reached.")
  })
  public Ticket requestTicket(@RequestParam("eventId") String eventId,
                              @RequestParam("email") String email, HttpServletResponse response) {
    Optional<Event> maybeEvent = eventService.findEvent(eventId);

    if (maybeEvent.isPresent()) {
      Event event = maybeEvent.get();

      if (ticketService.areTicketsRemaining(event)) {
        Ticket ticket = ticketService.createTicket(event, email);
        awsS3Service.generateAndUploadQrCodeTicket(ticket.getId());
        emailService.sendTicketEmail(ticket);

        return ticket;
      } else {
        GalaApiSpec.sendError(response, HttpStatus.SC_CONFLICT,
                "Event capacity " + "has already been reached.");
      }
    } else {
      GalaApiSpec.sendError(response, HttpStatus.SC_NOT_FOUND,
              "Event with id " + eventId + " could not be found.");
    }

    return null;
  }

  /**
   * Checks the status of the given Ticket and validates it if possible.
   *
   * @param ticketId Id of the ticket to be validated.
   * @param eventId Id of the event that the ticket is being validated for.
   * @param response Response passed in by Spring.
   */
  @PutMapping("/validate")
  @ResponseBody
  @ApiResponses(value = {
          @ApiResponse(code=HttpStatus.SC_OK, message = "Ticket successfully validated."),
          @ApiResponse(code=HttpStatus.SC_NOT_FOUND, message = "Ticket could not be found."),
          @ApiResponse(code=HttpStatus.SC_NOT_ACCEPTABLE, message = "Ticket could not be validated."),
          @ApiResponse(code=HttpStatus.SC_CONFLICT, message = "Ticket did not belong to the given event.")
  })
  public void validateTicket(@RequestParam("ticketId") String ticketId,
                             @RequestParam("eventId") String eventId, HttpServletResponse response) {
    Optional<Ticket> maybeTicket = ticketService.retrieveTicket(ticketId);
    if (maybeTicket.isPresent()) {
      Ticket ticket = maybeTicket.get();

      if (isTicketForEvent(eventId, ticket)) {
        switch (ticket.getStatus()) {
          case ACTIVE:
            ticketService.validateTicket(ticket);
            response.setStatus(HttpServletResponse.SC_OK);
            break;
          case VALIDATED:
            GalaApiSpec.sendError(response, HttpStatus.SC_NOT_ACCEPTABLE,
                    "Ticket with Id " + ticketId + " has already been validated. Could not validate.");
            break;
        }
      } else {
        GalaApiSpec.sendError(response, HttpServletResponse.SC_CONFLICT,
                "The given ticket is not for the given event.");
      }
    } else {
      GalaApiSpec.sendError(response, HttpServletResponse.SC_NOT_FOUND,
              "The given ticket cannot be found.");
    }
  }

  private boolean isTicketForEvent(String eventId, Ticket ticket) {
    return ticket.getEvent().getId().equals(eventId);
  }

  @Autowired
  public void setTicketService(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @Autowired
  public void setEventService(EventService eventService) {
    this.eventService = eventService;
  }

  @Autowired
  public void setAwsS3Service(AwsS3Service awsS3Service) {
    this.awsS3Service = awsS3Service;
  }

  @Autowired
  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }
}