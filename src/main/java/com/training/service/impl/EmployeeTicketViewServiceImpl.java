package com.training.service.impl;

import com.training.dto.view.EmployeeTicketViewDto;
import com.training.entity.view.EmployeeTicketView;
import com.training.mapper.ViewMapper;
import com.training.repository.EmployeeTicketViewRepository;
import com.training.service.EmployeeTicketViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeTicketViewServiceImpl implements EmployeeTicketViewService {

    private final EmployeeTicketViewRepository viewRepository;
    private final ViewMapper viewMapper;

    @Override
    public List<EmployeeTicketViewDto> findAllEmployeeTickets() {
        return viewMapper.toDto(viewRepository.findAll());
    }
}
