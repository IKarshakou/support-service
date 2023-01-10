package com.training.repository;

import com.training.entity.Ticket;
import com.training.entity.enums.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID>, JpaSpecificationExecutor<Ticket> {

    @Query("SELECT t FROM Ticket t WHERE t.owner.id = :userId")
    Page<Ticket> findAllByEmployee(UUID userId, Pageable pageable);

    @Query("SELECT t FROM Ticket t, User u WHERE (t.owner.id = u.id) "
            + "AND ((t.owner.id = :userId) "
            + "OR (u.role = 'EMPLOYEE' AND t.state = 'NEW') "
            + "OR (t.approver.id = :userId AND t.state IN :states))")
    Page<Ticket> findAllByManager(UUID userId, List<State> states, Pageable pageable);

    @Query("SELECT t FROM Ticket t, User u WHERE (t.owner.id = u.id) "
            + "AND (((t.owner.role = 'EMPLOYEE' OR t.owner.role = 'MANAGER') AND t.state = 'APPROVED') "
            + "OR (t.assignee.id = :userId AND (t.state = 'IN_PROGRESS' OR t.state = 'DONE')))")
    Page<Ticket> findAllByEngineer(UUID userId, Pageable pageable);
}
