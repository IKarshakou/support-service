package com.training.repository;

import com.training.entity.enums.State;
import com.training.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.owner.id = :userId ORDER BY t.id")
    List<Ticket> findAllByEmployee(Long userId);

    @Query("SELECT t FROM Ticket t, User u WHERE (t.owner.id = u.id) "
            + "AND ((t.owner.id = :userId) "
            + "OR (u.role = 'EMPLOYEE' AND t.state = 'NEW') "
            + "OR (t.approver.id = :userId AND t.state IN :states)) "
            + "ORDER BY t.id")
    List<Ticket> findAllByManager(Long userId, List<State> states);

    @Query("SELECT t FROM Ticket t, User u WHERE (t.owner.id = u.id) "
            + "AND (((t.owner.role = 'EMPLOYEE' OR t.owner.role = 'MANAGER') AND t.state = 'APPROVED') "
            + "OR (t.assignee.id = :userId AND (t.state = 'IN_PROGRESS' OR t.state = 'DONE'))) "
            + "ORDER BY t.id")
    List<Ticket> findAllByEngineer(Long userId);
}
