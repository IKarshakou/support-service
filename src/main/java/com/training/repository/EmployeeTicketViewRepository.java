package com.training.repository;

import com.training.entity.view.EmployeeTicketView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeTicketViewRepository extends JpaRepository<EmployeeTicketView, UUID> {
}
