package gala.gala_api.controller;

import javax.transaction.Transactional;

import gala.gala_api.dao.AccountCrudDao;
import gala.gala_api.dao.TicketCrudDao;
import gala.gala_api.service.email.SendTicketEmail;
import gala.gala_api.entity.Account;
import gala.gala_api.entity.Ticket;
import gala.gala_api.entity.TicketStatus;
import gala.gala_api.service.email.EmailService;
import gala.gala_api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.entity.Event;

import java.util.Date;

@RestController
@Transactional
public class ApplicationController  {

  @Autowired
  private EventCrudDao eventCrudDao;

  @Autowired
  private TicketCrudDao ticketCrudDao;

  @Autowired
  private AccountCrudDao accountCrudDao;

  @Autowired
  private EmailService emailService;

  @Autowired
  private TicketService ticketService;

  @GetMapping("/")
  public String index() {
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
    newTicket2.setEvent(newEvent);;

    this.accountCrudDao.save(newAccount);

    this.eventCrudDao.save(newEvent);

    this.ticketCrudDao.save(newTicket1);
    this.ticketCrudDao.save(newTicket2);

    return newTicket1.getId();
  }

  @GetMapping("/email")
  public String emailTest() {
    emailService.sendEmail("jmastrangelo111@gmail.com", new SendTicketEmail("Loft Gala!", "Null"));
    return "Email maybe sent idk fam";
  }

  @GetMapping("/test")
  public String ticketTest() {
    Account newAccount = new Account();
    newAccount.setFirstName("Jack");
    newAccount.setLastName("Mastrangelo");
    newAccount.setEmail("bite@my.shinymetalass");
    newAccount.setPassword("shhhhhh");

    accountCrudDao.save(newAccount);

    Event newEvent = new Event();
    newEvent.setName("A I D A C A");
    newEvent.setPlace("NoWhere");
    newEvent.setEventTime(new Date());
    newEvent.setCapacity(500);
    newEvent.setAccount(newAccount);

    eventCrudDao.save(newEvent);

    ticketService.createTicket(newEvent.getId(), "jmastrangelo111@gmail.com");

    return "Test Email Sent";
  }

}