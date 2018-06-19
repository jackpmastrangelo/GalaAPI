package gala.gala_api.service;

import org.junit.Test;

import gala.gala_api.dao.TicketCrudDao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class TicketServiceTest {

  @Test
  public void testAreTicketsRemaining() {
    TicketService service = new TicketService();

    TicketCrudDao ticketCrudDao = mock(TicketCrudDao.class);
    service.setTicketCrudDao(ticketCrudDao);
  }

}