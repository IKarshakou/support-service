package com.training.service;

import com.training.entity.view.ManagerTicketMatView;

import java.util.List;

public interface ManagerTicketMatViewService {
    List<ManagerTicketMatView> findAllManagerTickets();
}
