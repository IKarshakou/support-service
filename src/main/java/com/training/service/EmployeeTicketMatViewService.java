package com.training.service;

import com.training.entity.view.EmployeeTicketMatView;

import java.util.List;

public interface EmployeeTicketMatViewService {
    List<EmployeeTicketMatView> findAllEmployeeTickets();
}
