package com.training.service.impl;

import com.training.entity.view.ManagerTicketView;
import com.training.repository.ManagerTicketViewRepository;
import com.training.service.ManagerTicketViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerTicketViewServiceImpl implements ManagerTicketViewService {

    private final ManagerTicketViewRepository viewRepository;

    @Override
    public List<ManagerTicketView> findAllManagerTickets() {
        return viewRepository.findAll();
    }
}
