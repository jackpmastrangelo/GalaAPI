package gala.gala_api.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;

import gala.gala_api.entity.Account;
import gala.gala_api.entity.Event;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventCrudDaoTest {

  @Autowired
  TestEntityManager testEntityManager;

  @Autowired
  EventCrudDao eventCrudDao;

  @Test
  public void testFindByAccount() {
    String email = "gala@example.com";

    Account account = new Account();
    account.setFirstName("firstname");
    account.setLastName("lastname");
    account.setEmail(email);
    account.setPassword("password");
    testEntityManager.persist(account);

    Event event = new Event();
    event.setName("event");
    event.setPlace("place");
    event.setCapacity(10);
    event.setStartTime(new Date());
    event.setEndTime(new Date());
    event.setAccount(account);
    testEntityManager.persist(event);

    assertEquals(Arrays.asList(event), eventCrudDao.findByAccount(account));
  }
}