package com.training.controller;

import com.training.dto.history.OutputHistoryDto;
import com.training.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets/{ticketId}/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<OutputHistoryDto>> getTicketHistory(@PathVariable UUID ticketId) {
        return ResponseEntity.ok(historyService.findAllByTicketId(ticketId));
    }
}
