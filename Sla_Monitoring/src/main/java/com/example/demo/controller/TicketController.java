package com.example.demo.controller;

import com.example.demo.model.Ticket;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // 1. Create Ticket: POST http://localhost:8080/api/tickets?userName=Kalaivani
    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket, @RequestParam String userName) {
        return ticketService.createAndAssignTicket(ticket, userName);
    }

    // 2. All Tickets: GET http://localhost:8080/api/tickets
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    // 3. Manager View (By Domain): GET http://localhost:8080/api/tickets/domain/Software
    @GetMapping("/domain/{domain}")
    public List<Ticket> getTicketsByDomain(@PathVariable String domain) {
        return ticketService.getTicketsByDomain(domain);
    }

    // 4. Resolve Ticket: PUT http://localhost:8080/api/tickets/1/resolve
    @PutMapping("/{id}/resolve")
    public Ticket resolveTicket(@PathVariable Long id) {
        return ticketService.resolveTicket(id);
    }

    // 5. Support Decline Request: PUT http://localhost:8080/api/tickets/1/decline?reason=TooBusy
    @PutMapping("/{id}/decline")
    public Ticket declineTicket(@PathVariable Long id, @RequestParam String reason) {
        return ticketService.declineTicket(id, reason);
    }

    // 6. Manager Action on Decline: PUT http://localhost:8080/api/tickets/1/manager-action?action=APPROVE&newSupport=Prakash
    @PutMapping("/{id}/manager-action")
    public Ticket managerAction(
            @PathVariable Long id,
            @RequestParam String action,
            @RequestParam(required = false) String newSupport) {
        return ticketService.managerDeclineAction(id, action, newSupport);
    }

    // 7. Support Dashboard: GET http://localhost:8080/api/tickets/my-tickets/Arun
    @GetMapping("/my-tickets/{name}")
    public List<Ticket> getMyTickets(@PathVariable String name) {
        return ticketService.getTicketsByAssignee(name);
    }

    // 8. User Dashboard: GET http://localhost:8080/api/tickets/user-tickets/Kalaivani
    @GetMapping("/user-tickets/{userName}")
    public List<Ticket> getUserTickets(@PathVariable String userName) {
        return ticketService.getTicketsByCreator(userName);
    }
}