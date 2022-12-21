package com.training.service.impl;

import com.training.dto.feedback.InputFeedbackDto;
import com.training.dto.feedback.OutputFeedbackDto;
import com.training.entity.Feedback;
import com.training.entity.User;
import com.training.entity.enums.State;
import com.training.exception.TicketNotAvailableException;
import com.training.mapper.FeedbackMapper;
import com.training.repository.FeedbackRepository;
import com.training.repository.TicketRepository;
import com.training.security.UserPrincipal;
import com.training.service.FeedbackService;
import com.training.service.MailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private static final String TICKET_NOT_FOUND_MSG = "Ticket not found.";
    private static final String NOT_ENOUGH_PERMISSIONS_MSG = "Insufficient permissions.";
    private static final String FEEDBACK_NOT_FOUND_MSG = "Feedback not found.";

    private static final String FEEDBACK_PROVIDED_SUBJECT = "Feedback was provided";

    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;
    private final MailService mailService;
    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public void addFeedback(Long ticketId, InputFeedbackDto inputFeedbackDto) {
        var feedback = feedbackMapper.convertToEntity(inputFeedbackDto);
        var ticket = ticketRepository
                .findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND_MSG));

        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        var user = User.builder()
                .id(userPrincipal.getId())
                .build();

        if (ticket.getOwner().getId().equals(user.getId())
                && ticket.getState().equals(State.DONE)
                && !feedbackRepository.isFeedbackExistsByTicketId(ticketId)) {

            feedback.setUser(user);
            feedback.setTicket(ticket);

            feedbackRepository.save(feedback);
            mailService
                    .sendTicketHandlingEmail(List.of(ticket.getAssignee()), ticket, FEEDBACK_PROVIDED_SUBJECT);
        } else {
            throw new TicketNotAvailableException(NOT_ENOUGH_PERMISSIONS_MSG);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OutputFeedbackDto getFeedback(Long ticketId) {
        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        var ticket = ticketRepository
                .findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND_MSG));

        Feedback feedback;
        if (ticket.getAssignee() != null && ticket.getAssignee().getId().equals(userPrincipal.getId())) {
            feedback = feedbackRepository
                    .findByTicketId(ticketId)
                    .orElseThrow(() -> new EntityNotFoundException(FEEDBACK_NOT_FOUND_MSG));
        } else {
            throw new TicketNotAvailableException(NOT_ENOUGH_PERMISSIONS_MSG);
        }

        return feedbackMapper.convertToDto(feedback);
    }
}
