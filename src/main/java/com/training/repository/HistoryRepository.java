package com.training.repository;

import com.training.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("SELECT h FROM History h WHERE h.ticket.id = :ticketId ORDER BY h.date DESC")
    public List<History> findAllByTicketId(Long ticketId);
}
