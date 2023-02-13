package com.training.controller;

import com.training.dto.ticket.InputDraftTicketDto;
import com.training.dto.ticket.InputTicketDto;
import com.training.dto.ticket.OutputTicketDto;
import com.training.dto.ticket.OutputTicketWithDetailsDto;
import com.training.service.TicketService;
import com.training.validator.ErrorsChecker;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private static final String MY_TICKETS_PATH = "/myTickets";

    private final TicketService ticketService;
    private final ErrorsChecker errorsChecker;

    @GetMapping
    public ResponseEntity<List<OutputTicketDto>> getTickets(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "name") String sort) {
        return ResponseEntity.ok(ticketService.findAll(page, size, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutputTicketWithDetailsDto> getTicketById(@PathVariable("id") UUID ticketId) {
        return ResponseEntity.ok(ticketService.findById(ticketId));
    }

    @PatchMapping("/{id}/{action}")
    public ResponseEntity<Void> changeTicketState(
            @PathVariable("id") UUID ticketId,
            @PathVariable("action") String action) {
        ticketService.changeState(ticketId, action);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    public ResponseEntity<OutputTicketDto> submitNewTicket(
            @Validated @RequestPart("ticket") InputTicketDto inputTicketDto,
            @RequestPart(name = "attachments", required = false) List<MultipartFile> attachments,
            Errors errors) {

        errorsChecker.checkErrors(errors);
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

        errorsChecker.checkErrors(errors);
        return ResponseEntity
                .created(URI.create(MY_TICKETS_PATH))
                .body(ticketService.saveTicketAsDraft(inputTicketDto, attachments));
    }
}
