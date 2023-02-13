package com.training.service.impl;

import com.training.entity.view.EmployeeTicketMatView;
import com.training.repository.EmployeeTicketMatViewRepository;
import com.training.service.EmployeeTicketMatViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeTicketMatViewServiceImpl implements EmployeeTicketMatViewService {

    private final EmployeeTicketMatViewRepository viewRepository;

    @Override
    public List<EmployeeTicketMatView> findAllEmployeeTickets() {
        return viewRepository.findAll();
    }

}
