package gala.gala_api.controller;

import gala.gala_api.responses.ValidateTicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import gala.gala_api.responses.CreateTicketResponse;
import gala.gala_api.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

  @Autowired
  private TicketService ticketService;

  @PostMapping("/create")
  public CreateTicketResponse requestTicket(@RequestParam("eventId") Long eventId, @RequestParam("email") String email) {
    return ticketService.createTicket(eventId, email);
  }

  @PutMapping("/validate")
  public ValidateTicketResponse validateTicket(@RequestParam("ticketId") Long ticketId) {
    return ticketService.validateTicket(ticketId);
  }
}