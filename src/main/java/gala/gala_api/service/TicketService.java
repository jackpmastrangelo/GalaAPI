package gala.gala_api.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.dao.TicketCrudDao;
import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;
import gala.gala_api.entity.TicketStatus;
import gala.gala_api.responses.CreateTicketResponse;
import gala.gala_api.responses.ValidateTicketResponse;

@Transactional
@Component
public class TicketService {

  @Autowired
  private TicketCrudDao ticketCrudDao;

  @Autowired
  private EventCrudDao eventCrudDao;

	public CreateTicketResponse createTicket(Long eventId, String email) {
    CreateTicketResponse response = new CreateTicketResponse();
    Ticket newTicket = new Ticket();
    newTicket.setEmail(email);
    newTicket.setStatus(TicketStatus.ACTIVE); //Might not be always the case, but for now makes sense.
    Optional<Event> maybeEvent = this.eventCrudDao.findById(eventId);
    Event event;

    if (maybeEvent.isPresent()) {
      event = maybeEvent.get();
    } else {
      response.setSuccess(false);
      response.setMessage("The event with Id: " + eventId.toString() + " could not be found");
      return response;
    }

    //TODO Emails with QR Code.

    if (event.getCapacity() > ticketCrudDao.findByEvent(event).size()) {
      newTicket.setEvent(event);
      response.setTicket(ticketCrudDao.save(newTicket));
      response.setSuccess(true);
      response.setMessage("Ticket successfully added.");
      return response;
    } else {
      response.setSuccess(false);
      response.setMessage("Event capacity has already been reached.");
      return response;
    }
  }

  public ValidateTicketResponse validateTicket(Long ticketId) {
    ValidateTicketResponse response = new ValidateTicketResponse();
    Ticket ticket;
    Optional<Ticket> maybeTicket = ticketCrudDao.findById(ticketId);

    if (maybeTicket.isPresent()) {
      ticket = maybeTicket.get();
    } else {
      response.setSuccess(false);
      response.setMessage("Ticket with Id " + ticketId.toString() + " could not be found.");
      return response;
    }

    if (ticket.getStatus() == TicketStatus.ACTIVE) {
      ticket.setStatus(TicketStatus.VALIDATED);
      ticketCrudDao.save(ticket);
      response.setSuccess(true);
      response.setMessage("Ticket with Id " + ticketId.toString() + " was successfully validated.");
      return response;
    } else if (ticket.getStatus() == TicketStatus.VOIDED) {
      response.setSuccess(false);
      response.setMessage("Ticket with Id " + ticketId.toString() + " was voided. Could not validate.");
      return response;
    } else if (ticket.getStatus() == TicketStatus.VALIDATED){
      response.setSuccess(false);
      response.setMessage("Ticket with Id " + ticketId.toString() + " has already been validated. Could not validate.");
      return response;
    } else {
      response.setSuccess(false);
      response.setMessage("Could not ascertain whether ticket with Id "
              + ticketId.toString() + " was active or not. Could not validate");
      return response;
    }
  }
}