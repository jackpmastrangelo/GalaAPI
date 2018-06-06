package gala.gala_api.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.dao.TicketCrudDao;
import gala.gala_api.entity.Account;
import gala.gala_api.entity.Ticket;
import gala.gala_api.entity.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.entity.Event;

@RestController
@Transactional
public class ApplicationController  {

  @Autowired
  private EventCrudDao eventCrudDao;

  @Autowired
  private TicketCrudDao ticketCrudDao;

  @Autowired
  private AccountCrudDao accountCrudDao;

  @GetMapping("/")
  public Event index() {
    Account newAccount = new Account();
    newAccount.setFirstName("Jack");
    newAccount.setLastName("Mastrangelo");
    newAccount.setEmail("bite@my.shinymetalass");
    newAccount.setPassword("shhhhhh");

    Event newEvent = new Event();
    newEvent.setName("Nothing");
    newEvent.setPlace("NoWhere");
    newEvent.setEventTime(new Date());
    newEvent.setCapacity(500);
    newEvent.setAccount(newAccount);

    Ticket newTicket1 = new Ticket();
    newTicket1.setEmail("EMAIL!");
    newTicket1.setStatus(TicketStatus.ACTIVE);
    newTicket1.setEvent(newEvent);

    Ticket newTicket2 = new Ticket();
    newTicket2.setEmail("mail@me.bitch!");
    newTicket2.setStatus(TicketStatus.VOIDED);
    newTicket2.setEvent(newEvent);

    newEvent.setTickets(Arrays.asList(newTicket1, newTicket2));

    this.accountCrudDao.save(newAccount);

    this.eventCrudDao.save(newEvent);

    this.ticketCrudDao.save(newTicket1);
    this.ticketCrudDao.save(newTicket2);

    return eventCrudDao.findById(1L).get();
  }

}