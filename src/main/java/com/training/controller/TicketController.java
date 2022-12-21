package com.training.controller;

import com.training.dto.ticket.InputDraftTicketDto;
import com.training.dto.ticket.InputTicketDto;
import com.training.dto.ticket.OutputTicketDto;
import com.training.dto.ticket.OutputTicketWithDetailsDto;
import com.training.service.ErrorsHandlerService;
import com.training.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@Slf4j
@RequiredArgsConstructor
public class TicketController {

    private static final String MY_TICKETS_PATH = "/myTickets";

    private final TicketService ticketService;
    private final ErrorsHandlerService errorsHandlerService;

    @GetMapping
    public ResponseEntity<List<OutputTicketDto>> getTickets() {
        return ResponseEntity.ok(ticketService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutputTicketWithDetailsDto> getTicketById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @PatchMapping("/{id}/{action}")
    public ResponseEntity<Void> changeTicketState(@PathVariable("id") Long id, @PathVariable("action") String action) {
        ticketService.changeState(id, action);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    public ResponseEntity<OutputTicketDto> submitNewTicket(
            @Validated @RequestPart("ticket") InputTicketDto inputTicketDto,
            @RequestPart(name = "attachments", required = false) List<MultipartFile> attachments,
            Errors errors) {

        errorsHandlerService.checkErrors(errors);
        return ResponseEntity
                .created(URI.create(MY_TICKETS_PATH))
                .body(ticketService.createNewTicket(inputTicketDto, attachments));
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    public ResponseEntity<OutputTicketDto> saveTicketAsDraft(
            @Validated @RequestPart("ticket") InputDraftTicketDto inputTicketDto,
            @RequestPart(name = "attachments", required = false) List<MultipartFile> attachments,
            Errors errors) {

        errorsHandlerService.checkErrors(errors);
        return ResponseEntity
                .created(URI.create(MY_TICKETS_PATH))
                .body(ticketService.saveTicketAsDraft(inputTicketDto, attachments));
    }
}
