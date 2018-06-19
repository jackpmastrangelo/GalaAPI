package gala.gala_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import gala.gala_api.service.TicketService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tickets")
public class TicketController {

  @Autowired
  private TicketService ticketService;

  @PostMapping("/create")
  public String requestTicket(@RequestParam("eventId") String eventId, @RequestParam("email") String email, HttpServletResponse response) {
    return ticketService.createTicket(eventId, email, response);
  }

  @PutMapping("/validate")
  @ResponseStatus
  public void validateTicket(@RequestParam("ticketId") String ticketId, @RequestParam("eventId") Long eventId, HttpServletResponse response) {
    ticketService.validateTicket(ticketId, eventId, response);
  }
}