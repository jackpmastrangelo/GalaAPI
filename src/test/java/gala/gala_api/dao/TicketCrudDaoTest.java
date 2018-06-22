package gala.gala_api.dao;

import gala.gala_api.entity.Account;
import gala.gala_api.entity.TicketStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TicketCrudDaoTest {

  @Autowired
  TestEntityManager testEntityManager;

  @Autowired
  TicketCrudDao ticketCrudDao;

  //TODO Can mockito help with faking all these values that need values.
  @Test
  public void testFindByEvent() {
    Account account = new Account();
    account.setFirstName("Jack");
    account.setLastName("M");
    account.setEmail("j@j.com");
    account.setPassword("ZippaDeeDooDa");
    Event event1 = new Event();
    event1.setAccount(account);
    event1.setName("e1");
    event1.setCapacity(20);
    Event event2 = new Event();
    event2.setAccount(account);
    event2.setName("e2");
    event2.setCapacity(25);
    Ticket ticket1 = new Ticket();
    ticket1.setEvent(event1);
    ticket1.setStatus(TicketStatus.ACTIVE);
    ticket1.setEmail("d@d.com");
    Ticket ticket2 = new Ticket();
    ticket2.setEvent(event1);
    ticket2.setStatus(TicketStatus.ACTIVE);
    ticket2.setEmail("d@d.com");
    Ticket ticket3 = new Ticket();
    ticket3.setEvent(event2);
    ticket3.setStatus(TicketStatus.ACTIVE);
    ticket3.setEmail("d@d.com");

    testEntityManager.persist(account);

    testEntityManager.persist(event1);
    testEntityManager.persist(event2);

    testEntityManager.persist(ticket1);
    testEntityManager.persist(ticket2);
    testEntityManager.persist(ticket3);

    List<Ticket> ticketList = ticketCrudDao.findByEvent(event1);

    assertEquals(true, ticketList.contains(ticket1));
    assertEquals(true, ticketList.contains(ticket2));
    assertEquals(false, ticketList.contains(ticket3));
    assertEquals(2, ticketList.size());
  }
}