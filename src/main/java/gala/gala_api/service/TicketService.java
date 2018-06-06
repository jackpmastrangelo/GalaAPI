package gala.gala_api.service;

import static org.junit.Assert.assertEquals;

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

        if (event.getCapacity() > event.getTickets().size()) {
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
        }

        if (ticket.getStatus() == TicketStatus.ACTIVE) {
            
        }

    }
}