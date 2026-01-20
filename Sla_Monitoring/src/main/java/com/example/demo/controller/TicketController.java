package com.example.demo.controller;

import com.example.demo.model.Ticket;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket, @RequestParam String userName) {
        return ticketService.createAndAssignTicket(ticket, userName);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/domain/{domain}")
    public List<Ticket> getTicketsByDomain(@PathVariable String domain) {
        return ticketService.getTicketsByDomain(domain);
    }

    @PutMapping("/{id}/resolve")
    public Ticket resolveTicket(@PathVariable Long id) {
        return ticketService.resolveTicket(id);
    }

    @PutMapping("/{id}/decline")
    public Ticket declineTicket(@PathVariable Long id, @RequestParam String reason) {
        return ticketService.declineTicket(id, reason);
    }

    @PutMapping("/{id}/manager-action")
    public Ticket managerAction(
            @PathVariable Long id,
            @RequestParam String action,
            @RequestParam(required = false) String newSupport) {
        return ticketService.managerDeclineAction(id, action, newSupport);
    }

    @GetMapping("/my-tickets/{name}")
    public List<Ticket> getMyTickets(@PathVariable String name) {
        return ticketService.getTicketsByAssignee(name);
    }

    @GetMapping("/user-tickets/{userName}")
    public List<Ticket> getUserTickets(@PathVariable String userName) {
        return ticketService.getTicketsByCreator(userName);
    }
}