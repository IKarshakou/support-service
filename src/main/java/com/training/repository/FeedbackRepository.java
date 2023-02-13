package com.training.repository;

import com.training.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    Optional<Feedback> findByTicketId(UUID ticketId);

    boolean existsFeedbackByTicketId(UUID ticketId);
}
