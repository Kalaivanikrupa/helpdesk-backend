package com.example.demo.repository;

import com.example.demo.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Indha import venum
import java.util.List;

@Repository // Indha annotation important
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // 1. Manager domain base panni tickets paarkka
    List<Ticket> findByDomain(String domain);

    // 2. Specific support member-ku assign aana tickets-ah mattum edukka
    // Ippo name mattum vechu oru method potta podhum
    List<Ticket> findByAssignedTo(String name);

    List<Ticket> findByCreatedBy(String userName);
}