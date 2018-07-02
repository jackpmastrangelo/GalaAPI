package gala.gala_api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import gala.gala_api.dao.TicketCrudDao;
import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;
import gala.gala_api.entity.TicketStatus;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class TicketService {

  private TicketCrudDao ticketCrudDao;

  public Ticket createTicket(Event event, String email) {
    Ticket ticket = new Ticket();
    ticket.setEvent(event);
    ticket.setEmail(email);
    ticket.setStatus(TicketStatus.ACTIVE);

    ticketCrudDao.save(ticket);

    return ticket;
  }

  public boolean areTicketsRemaining(Event event) {
    List<Ticket> existingTickets = ticketCrudDao.findByEvent(event);
    return event.getCapacity() > existingTickets.size();
  }

  public void validateTicket(Ticket ticket) {
    ticket.setStatus(TicketStatus.VALIDATED);
    ticketCrudDao.save(ticket);
  }

  public Optional<Ticket> retrieveTicket(String ticketId) {
    return ticketCrudDao.findById(ticketId);
  }

  @Autowired
  public void setTicketCrudDao(TicketCrudDao ticketCrudDao) {
    this.ticketCrudDao = ticketCrudDao;
  }
}