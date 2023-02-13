package com.training.service.impl;

import com.training.entity.view.ManagerTicketMatView;
import com.training.repository.ManagerTicketMatViewRepository;
import com.training.service.ManagerTicketMatViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerTicketMatViewServiceImpl implements ManagerTicketMatViewService {

    private final ManagerTicketMatViewRepository viewRepository;

    @Override
    public List<ManagerTicketMatView> findAllManagerTickets() {
        return viewRepository.findAll();
    }
}
