package com.training.service;

import com.training.dto.ticket.InputDraftTicketDto;
import com.training.dto.ticket.InputTicketDto;
import com.training.dto.ticket.OutputTicketDto;
import com.training.dto.ticket.OutputTicketWithDetailsDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {
    List<OutputTicketDto> findAll();

    OutputTicketWithDetailsDto findById(Long id);

    void changeState(Long id, String inputAction);

    OutputTicketDto createNewTicket(InputTicketDto inputTicketDto, List<MultipartFile> attachments);

    OutputTicketDto saveTicketAsDraft(InputDraftTicketDto inputTicketDto, List<MultipartFile> attachments);
}
