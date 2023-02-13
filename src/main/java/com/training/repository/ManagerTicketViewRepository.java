package com.training.repository;

import com.training.entity.view.ManagerTicketView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManagerTicketViewRepository extends JpaRepository<ManagerTicketView, UUID> {
}
