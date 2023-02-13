package com.training.service;

import com.training.entity.view.ManagerTicketView;

import java.util.List;

public interface ManagerTicketViewService {
    List<ManagerTicketView> findAllManagerTickets();
}
