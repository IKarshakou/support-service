package com.training.service.impl;

import com.training.dto.history.OutputHistoryDto;
import com.training.mapper.HistoryMapper;
import com.training.repository.HistoryRepository;
import com.training.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OutputHistoryDto> findAllByTicketId(Long ticketId) {
        var historyList = historyRepository.findAllByTicketId(ticketId);
        return historyList
                .stream()
                .map(historyMapper::convertToDto)
                .toList();
    }
}
