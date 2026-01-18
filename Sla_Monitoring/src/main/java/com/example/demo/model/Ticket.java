package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String domain; // Software, Network, etc.
    private String priority; // High, Medium, Low
    private String status; // Open, Assigned, Resolved, Escalated, Decline Requested

    private String slaLimit; // e.g., "4 hrs"
    private LocalDateTime createdAt;

    private String assignedTo; // Support member name
    private String assignmentType; // AUTO or MANUAL

    private String declineReason;
    private boolean transferApproved = false;

    private String createdBy; // Ticket-ah yaaru create panna nu store panna

    private String declineStatus; // "PENDING", "APPROVED", "REJECTED"

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "Open";
    }
}