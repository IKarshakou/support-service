package com.training.service;

import com.training.dto.view.EmployeeTicketViewDto;

import java.util.List;

public interface EmployeeTicketViewService {
    List<EmployeeTicketViewDto> findAllEmployeeTickets();
}
