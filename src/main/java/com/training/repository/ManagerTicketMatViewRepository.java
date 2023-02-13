package com.training.repository;

import com.training.entity.view.ManagerTicketMatView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManagerTicketMatViewRepository extends JpaRepository<ManagerTicketMatView, UUID> {
}
