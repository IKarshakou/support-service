package com.training.service.impl;

import com.training.dto.history.OutputHistoryDto;
import com.training.entity.History;
import com.training.mapper.HistoryMapper;
import com.training.repository.HistoryRepository;
import com.training.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;

    @Override
    public List<OutputHistoryDto> findAllByTicketId(Long ticketId) {
        List<History> history = historyRepository.findAllByTicketId(ticketId);
        return history
                .stream()
                .map(historyMapper::convertToDto)
                .toList();
    }
}
