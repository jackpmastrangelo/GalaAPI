package gala.gala_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import javax.servlet.http.HttpServletResponse;

import gala.gala_api.responses.CreateTicketResponse;
=======
>>>>>>> 481a61e20c14bb4d5ae860f3f939af88298e1800
import gala.gala_api.service.TicketService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tickets")
public class TicketController {

  @Autowired
  private TicketService ticketService;

  @PostMapping("/create")
  public String requestTicket(@RequestParam("event_id") String eventId, @RequestParam("email") String email, HttpServletResponse response) {
    return ticketService.createTicket(eventId, email, response);
  }

  @PutMapping("/validate")
  @ResponseStatus
  public void validateTicket(@RequestParam("ticket_id") String ticketId, @RequestParam("event_id") Long eventId, HttpServletResponse response) {
    ticketService.validateTicket(ticketId, eventId, response);
  }
}