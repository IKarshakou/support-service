package com.training.repository;

import com.training.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT feedback FROM Feedback feedback WHERE feedback.ticket.id = :ticketId")
    Optional<Feedback> findByTicketId(Long ticketId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 "
            + "THEN TRUE "
            + "ELSE FALSE END "
            + "FROM Feedback f WHERE f.ticket.id = :ticketId")
    boolean isFeedbackExistsByTicketId(Long ticketId);
}
