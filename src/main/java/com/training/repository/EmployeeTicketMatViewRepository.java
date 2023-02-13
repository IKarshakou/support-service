package com.training.repository;

import com.training.entity.view.EmployeeTicketMatView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeTicketMatViewRepository extends JpaRepository<EmployeeTicketMatView, UUID> {
    @Modifying
    @Query(value = "CALL refresh_employee_tickets_mat_view()", nativeQuery = true)
//    @Procedure(procedureName = "refresh_employee_tickets_mat_view")
    void refresh();
}
