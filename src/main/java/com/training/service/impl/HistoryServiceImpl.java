package com.training.service.impl;

import com.training.dto.history.OutputHistoryDto;
import com.training.mapper.HistoryMapper;
import com.training.repository.HistoryRepository;
import com.training.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OutputHistoryDto> findAllByTicketId(UUID ticketId) {
        var historyList = historyRepository.findAllByTicketIdOrderByDateDesc(ticketId);
        return historyList
                .stream()
                .map(historyMapper::convertToDto)
                .toList();
    }
}
