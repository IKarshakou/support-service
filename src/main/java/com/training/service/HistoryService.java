package com.training.service;

import com.training.dto.history.OutputHistoryDto;

import java.util.List;
import java.util.UUID;

public interface HistoryService {
    List<OutputHistoryDto> findAllByTicketId(UUID ticketId);
}
