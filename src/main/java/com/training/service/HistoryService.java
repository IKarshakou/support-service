package com.training.service;

import com.training.dto.history.OutputHistoryDto;

import java.util.List;

public interface HistoryService {
    List<OutputHistoryDto> findAllByTicketId(Long ticketId);
}
