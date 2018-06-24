package gala.gala_api.service;

import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;
import gala.gala_api.entity.TicketStatus;
import org.junit.Test;

import gala.gala_api.dao.TicketCrudDao;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketServiceTest {

  @Test
  public void testCreateTicket() {
    TicketService service = new TicketService();
    TicketCrudDao ticketCrudDao = mock(TicketCrudDao.class);

    service.setTicketCrudDao(ticketCrudDao);

    Event event = new Event();
    String email = "jack@galatix.io";

    Ticket ticket = service.createTicket(event, email);

    assertEquals("jack@galatix.io", ticket.getEmail());
    assertEquals(event, ticket.getEvent());
  }

  @Test
  public void testAreTicketsRemaining() {
    TicketService service = new TicketService();
    Event event = new Event();
    event.setCapacity(2);

    Event freeSpaceEvent = new Event();
    freeSpaceEvent.setCapacity(3);

    Ticket ticket1 = new Ticket();
    Ticket ticket2 = new Ticket();

    TicketCrudDao ticketCrudDao = mock(TicketCrudDao.class);
    when(ticketCrudDao.findByEvent(event)).thenReturn(Arrays.asList(ticket1, ticket2));

    service.setTicketCrudDao(ticketCrudDao);

    assertEquals(service.areTicketsRemaining(event), false);
    assertEquals(service.areTicketsRemaining(freeSpaceEvent), true);
  }

  @Test
  public void testValidateTicket() {
    TicketService service = new TicketService();
    TicketCrudDao ticketCrudDao = mock(TicketCrudDao.class);
    Ticket ticket = new Ticket();
    ticket.setStatus(TicketStatus.ACTIVE);

    when(ticketCrudDao.save(ticket)).thenReturn(ticket);

    service.setTicketCrudDao(ticketCrudDao);
    service.validateTicket(ticket);

    assertEquals(TicketStatus.VALIDATED, ticket.getStatus());
  }

  @Test
  public void testRetriveTicket() {
    TicketService service = new TicketService();
    TicketCrudDao ticketCrudDao = mock(TicketCrudDao.class);

    Ticket ticket = new Ticket();
    Optional<Ticket> maybeTicket = Optional.of(ticket);

    when(ticketCrudDao.findById("One")).thenReturn(maybeTicket);
    service.setTicketCrudDao(ticketCrudDao);

    assertEquals(maybeTicket.get(), service.retrieveTicket("One").get());
  }
}