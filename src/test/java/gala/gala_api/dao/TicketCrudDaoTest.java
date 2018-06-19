package gala.gala_api.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TicketCrudDaoTest {

  @Autowired
  private TestEntityManager entityManager;

  @Test
  public void testFindByEvent() {
    Event event = new Event();
    Ticket ticket = new Ticket();
    ticket.setEvent(event);



  }
}