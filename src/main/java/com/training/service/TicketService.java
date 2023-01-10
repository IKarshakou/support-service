package com.training.service;

import com.training.dto.ticket.InputDraftTicketDto;
import com.training.dto.ticket.InputTicketDto;
import com.training.dto.ticket.OutputTicketDto;
import com.training.dto.ticket.OutputTicketWithDetailsDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    OutputTicketWithDetailsDto findById(UUID id);

    void changeState(UUID id, String inputAction);

    OutputTicketDto createNewTicket(InputTicketDto inputTicketDto, List<MultipartFile> attachments);

    OutputTicketDto saveTicketAsDraft(InputDraftTicketDto inputTicketDto, List<MultipartFile> attachments);

    List<OutputTicketDto> findAll(int page, int size, String sort);
}
