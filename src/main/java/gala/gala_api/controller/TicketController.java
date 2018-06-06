package gala.gala_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gala.gala_api.responses.CreateTicketResponse;
import gala.gala_api.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService TicketService;

    @PostMapping("/create")
    public CreateTicketResponse requestTicket(@RequestParam("eventId") Long eventId, @RequestParam("email") String email) {
        return null;
    }

}