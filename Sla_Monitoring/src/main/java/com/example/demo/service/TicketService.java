package com.example.demo.service;

import com.example.demo.model.Ticket;
import com.example.demo.model.User;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Unified Create Ticket Logic (SLA + Auto-Assign + CreatedBy)
    public Ticket createAndAssignTicket(Ticket ticket, String userName) {
        ticket.setCreatedBy(userName);

        // SLA logic...
        if(ticket.getPriority().equalsIgnoreCase("High")) ticket.setSlaLimit("4 hrs");
        else if(ticket.getPriority().equalsIgnoreCase("Medium")) ticket.setSlaLimit("24 hrs");
        else ticket.setSlaLimit("72 hrs");

        List<User> support = userRepository.findByDomainIgnoreCaseAndRoleAndAvailableTrue(ticket.getDomain(), "SUPPORT");

        if (!support.isEmpty()) {
            User selectedSupport = support.get(0); // First available person
            ticket.setAssignedTo(selectedSupport.getName());
            ticket.setStatus("Assigned");
            ticket.setAssignmentType("AUTO");

            // *** INTHA LINE-AH ADD PANNUNGA ***
            selectedSupport.setAvailable(false); // Avarai busy-ah mathurom
            userRepository.save(selectedSupport);

        } else {
            userRepository.findByDomainAndRole(ticket.getDomain(), "MANAGER").ifPresent(m -> {
                ticket.setAssignedTo(m.getName());
                ticket.setStatus("Open");
                ticket.setAssignmentType("AUTO_TO_MANAGER");
            });
        }
        return ticketRepository.save(ticket);
    }

    // 2. SLA Escalation
    @Scheduled(fixedRate = 60000)
    public void checkSLAEscalation() {
        List<Ticket> activeTickets = ticketRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Ticket t : activeTickets) {
            if ("Assigned".equals(t.getStatus()) && t.getSlaLimit() != null) {
                try {
                    int limitHours = Integer.parseInt(t.getSlaLimit().split(" ")[0]);
                    if (t.getCreatedAt().plusHours(limitHours).isBefore(now)) {
                        t.setStatus("Escalated");
                        ticketRepository.save(t);
                    }
                } catch (Exception e) { /* ignore parse error */ }
            }
        }
    }

    // 3. Status Actions
    public Ticket resolveTicket(Long id) {
        Ticket t = ticketRepository.findById(id).orElseThrow();
        t.setStatus("Resolved");

        // *** INTHA LOGIC-AH ADD PANNUNGA ***
        // Ticket assign aagi irukkura person name-ah vechu User-ah thedurom
        userRepository.findByName(t.getAssignedTo()).ifPresent(u -> {
            if ("SUPPORT".equalsIgnoreCase(u.getRole())) {
                u.setAvailable(true); // Thirumba available list-ku kondu varom
                userRepository.save(u);
            }
        });

        return ticketRepository.save(t);
    }
    public Ticket declineTicket(Long id, String reason) {
        Ticket t = ticketRepository.findById(id).orElseThrow();
        t.setDeclineReason(reason);
        t.setDeclineStatus("PENDING");
        t.setStatus("Awaiting Manager Approval");
        return ticketRepository.save(t);
    }

    public Ticket managerDeclineAction(Long id, String action, String newSupportName) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (action.equalsIgnoreCase("APPROVE")) {
            // 1. Modhalla pazhaya support person-ah free pannanum (Arun becomes available)
            userRepository.findByName(ticket.getAssignedTo()).ifPresent(oldUser -> {
                if ("SUPPORT".equalsIgnoreCase(oldUser.getRole())) {
                    oldUser.setAvailable(true);
                    userRepository.save(oldUser);
                }
            });

            // 2. Logic for Option 3: Manager Takes Responsibility if no support is chosen
            if (newSupportName == null || newSupportName.isEmpty()) {
                // Yaarumae illana, andha domain manager-ah kandupidippom
                User manager = userRepository.findByDomainAndRole(ticket.getDomain(), "MANAGER")
                        .orElseThrow(() -> new RuntimeException("Manager not found for this domain"));

                ticket.setAssignedTo(manager.getName());
                ticket.setStatus("Open"); // Support kitta illatha vara idhu "Open" status
                ticket.setAssignmentType("MANAGER_HANDLING");
            } else {
                // Support member select panni irundha, avangalukku transfer pannuvom
                ticket.setAssignedTo(newSupportName);
                ticket.setStatus("Assigned");

                // Pudhu support member-ah busy pannanum
                userRepository.findByName(newSupportName).ifPresent(newUser -> {
                    newUser.setAvailable(false);
                    userRepository.save(newUser);
                });
            }

            ticket.setDeclineStatus("APPROVED");
            ticket.setTransferApproved(true);
            ticket.setDeclineReason(null);

        } else {
            // Reject panna, ticket thirumba old support kitta poirum (Avar already busy status-la dhaan irupparu)
            ticket.setDeclineStatus("REJECTED");
            ticket.setStatus("Assigned");
        }

        return ticketRepository.save(ticket);
    }

    // 4. Filters
    public List<Ticket> getAllTickets() { return ticketRepository.findAll(); }
    public List<Ticket> getTicketsByDomain(String domain) { return ticketRepository.findByDomain(domain); }
    public List<Ticket> getTicketsByAssignee(String name) { return ticketRepository.findByAssignedTo(name); }
    public List<Ticket> getTicketsByCreator(String name) { return ticketRepository.findByCreatedBy(name); }
}