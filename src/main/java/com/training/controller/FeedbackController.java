package com.training.controller;

import com.training.dto.feedback.InputFeedbackDto;
import com.training.dto.feedback.OutputFeedbackDto;
import com.training.validator.ErrorsChecker;
import com.training.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tickets/{id}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private static final String MY_TICKETS_PATH = "/myTickets";
    private static final String SLASH = "/";

    private final FeedbackService feedbackService;
    private final ErrorsChecker errorsChecker;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    public ResponseEntity<Void> leaveFeedback(@PathVariable("id") UUID ticketId,
                                              @Validated @RequestBody InputFeedbackDto inputFeedbackDto,
                                              Errors errors) {
        errorsChecker.checkErrors(errors);
        feedbackService.addFeedback(ticketId, inputFeedbackDto);
        return ResponseEntity
                .created(URI.create(MY_TICKETS_PATH + SLASH + ticketId)).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ENGINEER')")
    public ResponseEntity<OutputFeedbackDto> getFeedback(@PathVariable("id") UUID ticketId) {
        return ResponseEntity.ok(feedbackService.getFeedback(ticketId));
    }
}
