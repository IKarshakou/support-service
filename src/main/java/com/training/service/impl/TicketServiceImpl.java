package com.training.service.impl;

import com.training.dto.ticket.InputDraftTicketDto;
import com.training.dto.ticket.InputTicketDto;
import com.training.dto.ticket.OutputTicketDto;
import com.training.dto.ticket.OutputTicketWithDetailsDto;
import com.training.entity.History;
import com.training.entity.Ticket;
import com.training.entity.User;
import com.training.entity.enums.Action;
import com.training.entity.enums.Role;
import com.training.entity.enums.State;
import com.training.exception.TicketNotAvailableException;
import com.training.mapper.TicketMapper;
import com.training.repository.CategoryRepository;
import com.training.repository.TicketRepository;
import com.training.repository.UserRepository;
import com.training.security.UserPrincipal;
import com.training.service.AttachmentService;
import com.training.service.MailService;
import com.training.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private static final String NO_TICKETS_MSG = "No tickets.";
    private static final String TICKET_NOT_FOUND_MSG = "Ticket not found.";
    private static final String CATEGORY_NOT_FOUND_MSG = "Category not found.";
    private static final String NOT_ENOUGH_PERMISSIONS_MSG = "Insufficient permissions.";
    private static final String INCORRECT_ACTION_MSG = "Incorrect input action.";

    private static final String TICKET_CREATED_ACTION = "Ticket is created.";
    private static final String TICKET_EDITED_ACTION = "Ticket is edited.";
    private static final String TICKET_STATUS_CHANGED_ACTION = "Ticket Status is changed.";
    private static final String TICKET_STATUS_CHANGED_DESCRIPTION = "Ticket Status is changed from [%s] to [%s].";

    private static final String NEW_TICKET_SUBJECT = "New ticket for approval";
    private static final String TICKET_APPROVED_SUBJECT = "Ticket was approved";
    private static final String TICKET_DECLINED_SUBJECT = "Ticket was declined";
    private static final String TICKET_CANCELLED_SUBJECT = "Ticket was cancelled";
    private static final String TICKET_DONE_SUBJECT = "Ticket was done";

    private static final String ROLE_PREFIX = "ROLE_";

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final AttachmentService attachmentService;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OutputTicketDto> findAll() {
        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        var authoritiesList = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        List<Ticket> tickets;
        if (authoritiesList.contains(ROLE_PREFIX + Role.EMPLOYEE.name())) {
            tickets = ticketRepository.findAllByEmployee(userPrincipal.getId());
        } else if (authoritiesList.contains(ROLE_PREFIX + Role.MANAGER.name())) {
            tickets = ticketRepository.findAllByManager(userPrincipal.getId(), List.of(
                    State.APPROVED,
                    State.DECLINED,
                    State.CANCELED,
                    State.IN_PROGRESS,
                    State.DONE));
        } else {
            tickets = ticketRepository.findAllByEngineer(userPrincipal.getId());
        }

        if (tickets.isEmpty()) {
            throw new EntityNotFoundException(NO_TICKETS_MSG);
        }

        return ticketMapper.convertListToDto(tickets);
    }

    @Override
    @Transactional(readOnly = true)
    public OutputTicketWithDetailsDto findById(Long id) {
        return ticketMapper.convertToTicketWithDetailsDto(ticketRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND_MSG)));
    }

    @Override
    @Transactional
    public void changeState(Long id, String inputAction) {
        var ticket = ticketRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND_MSG));

        Action action;
        if (inputAction != null) {
            action = Enum.valueOf(Action.class, inputAction.toUpperCase());
        } else {
            throw new IllegalArgumentException(INCORRECT_ACTION_MSG);
        }

        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        switch (action) {
            case APPROVE -> approveTicket(ticket, userPrincipal);
            case DECLINE -> declineTicket(ticket, userPrincipal);
            case CANCEL -> cancelTicket(ticket, userPrincipal);
            case ASSIGN -> assignTicket(ticket, userPrincipal);
            case DONE -> doneTicket(ticket, userPrincipal);
        }

        ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public OutputTicketDto createNewTicket(InputTicketDto inputTicketDto, List<MultipartFile> attachments) {
        var ticket = ticketMapper.convertToEntity(inputTicketDto, List.of(inputTicketDto.getComment()));
        ticket.setState(State.NEW);

        return saveTicketToDatabase(ticket, attachments);
    }

    @Override
    @Transactional
    public OutputTicketDto saveTicketAsDraft(InputDraftTicketDto inputTicketDto, List<MultipartFile> attachments) {
        var ticket = ticketMapper.convertDraftToEntity(inputTicketDto, List.of(inputTicketDto.getComment()));
        ticket.setState(State.DRAFT);

        return saveTicketToDatabase(ticket, attachments);
    }

    private OutputTicketDto saveTicketToDatabase(Ticket ticket, List<MultipartFile> attachments) {
        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        var user = User.builder()
                .id(userPrincipal.getId())
                .build();

        var category = categoryRepository
                .findByName(ticket.getCategory().getName())
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND_MSG));

        attachmentService.uploadAttachmentsToServer(ticket, attachments);
        handleTicketComment(ticket, user);

        if (ticket.getId() != null) {
            var ticketFromDatabase = ticketRepository
                    .findById(ticket.getId())
                    .orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND_MSG));

            if (!userIsOwner(userPrincipal, ticketFromDatabase)) {
                throw new TicketNotAvailableException(NOT_ENOUGH_PERMISSIONS_MSG);
            }

            addEditionHistoryToTicket(ticket, ticketFromDatabase, user);
        } else {
            addCreationHistoryToTicket(ticket, user);
        }

        ticket.setOwner(user);
        ticket.setCategory(category);

        ticketRepository.save(ticket);

        if (isNew(ticket)) {
            List<User> recipients = userRepository.findAllManagers();
            mailService.sendTicketHandlingEmail(recipients, ticket, NEW_TICKET_SUBJECT);
        }

        return ticketMapper.convertToDto(ticket);
    }

    private void handleTicketComment(Ticket ticket, User user) {
        if (!ticket.getComments().isEmpty()) {
            var comment = ticket.getComments().get(ticket.getComments().size() - 1);
            if (!comment.getText().isBlank()) {
                comment.setUser(user);
                comment.setTicket(ticket);
            } else {
                ticket.getComments().remove(comment);
            }
        }
    }

    private void addCreationHistoryToTicket(Ticket ticket, User user) {
        var ticketCreatedHistory = History.builder()
                .ticket(ticket)
                .action(TICKET_CREATED_ACTION)
                .description(TICKET_CREATED_ACTION)
                .user(user)
                .build();
        ticket.getHistory().add(ticketCreatedHistory);
    }

    private void addEditionHistoryToTicket(Ticket ticket, Ticket ticketFromDatabase, User user) {
        var ticketEditedHistory = History.builder()
                .ticket(ticket)
                .action(TICKET_EDITED_ACTION)
                .description(TICKET_EDITED_ACTION)
                .user(user)
                .build();
        ticket.getHistory().add(ticketEditedHistory);

        if (isNew(ticket)) {
            var ticketStateHistory = History.builder()
                    .ticket(ticket)
                    .action(TICKET_STATUS_CHANGED_ACTION)
                    .description(TICKET_STATUS_CHANGED_DESCRIPTION.formatted(
                            ticketFromDatabase.getState().name(),
                            ticket.getState().name()))
                    .user(user)
                    .build();
            ticket.getHistory().add(ticketStateHistory);
        }
    }

    private void approveTicket(Ticket ticket, UserPrincipal userPrincipal) {
        if ((userIsManager(userPrincipal) && isNew(ticket))
                && (employeeIsOwner(ticket) || userIsOwner(userPrincipal, ticket))) {

            var approver = User.builder().id(userPrincipal.getId()).build();

            var ticketStateHistory = History.builder()
                    .user(approver)
                    .ticket(ticket)
                    .action(TICKET_STATUS_CHANGED_ACTION)
                    .description(TICKET_STATUS_CHANGED_DESCRIPTION.formatted(
                            ticket.getState().name(),
                            State.APPROVED.name()))
                    .build();
            ticket.setState(State.APPROVED);
            ticket.setApprover(approver);
            ticket.getHistory().add(ticketStateHistory);

            List<User> recipients = userRepository.findAllEngineers();
            recipients.add(ticket.getOwner());
            mailService.sendTicketHandlingEmail(recipients, ticket, TICKET_APPROVED_SUBJECT);
        } else {
            throw new TicketNotAvailableException(NOT_ENOUGH_PERMISSIONS_MSG);
        }
    }

    private void declineTicket(Ticket ticket, UserPrincipal userPrincipal) {
        if ((userIsManager(userPrincipal) && isNew(ticket))
                && (employeeIsOwner(ticket) || userIsOwner(userPrincipal, ticket))) {

            var ticketStateHistory = History.builder()
                    .user(User.builder().id(userPrincipal.getId()).build())
                    .ticket(ticket)
                    .action(TICKET_STATUS_CHANGED_ACTION)
                    .description(TICKET_STATUS_CHANGED_DESCRIPTION.formatted(
                            ticket.getState().name(),
                            State.DECLINED.name()))
                    .build();
            ticket.setState(State.DECLINED);
            ticket.getHistory().add(ticketStateHistory);

            mailService.sendTicketHandlingEmail(List.of(ticket.getOwner()), ticket, TICKET_DECLINED_SUBJECT);
        } else {
            throw new TicketNotAvailableException(NOT_ENOUGH_PERMISSIONS_MSG);
        }
    }

    private void cancelTicket(Ticket ticket, UserPrincipal userPrincipal) {
        if ((userIsOwner(userPrincipal, ticket) && employeeIsOwner(ticket)
                && (ticket.getState().equals(State.DRAFT) || ticket.getState().equals(State.DECLINED)))

                || (userIsManager(userPrincipal) && userIsOwner(userPrincipal, ticket)
                && (ticket.getState().equals(State.DRAFT) || ticket.getState().equals(State.DECLINED)))

                || (userIsManager(userPrincipal) && employeeIsOwner(ticket)
                && ticket.getState().equals(State.NEW))

                || (userIsEngineer(userPrincipal)
                && ticket.getState().equals(State.APPROVED))) {

            var ticketStateHistory = History.builder()
                    .user(User.builder().id(userPrincipal.getId()).build())
                    .ticket(ticket)
                    .action(TICKET_STATUS_CHANGED_ACTION)
                    .description(TICKET_STATUS_CHANGED_DESCRIPTION.formatted(
                            ticket.getState().name(),
                            State.CANCELED.name()))
                    .build();
            var previousState = ticket.getState();
            ticket.setState(State.CANCELED);
            ticket.getHistory().add(ticketStateHistory);

            List<User> recipients = new ArrayList<>();
            if (previousState.equals(State.NEW)) {
                recipients = List.of(ticket.getOwner());
            } else if (previousState.equals(State.APPROVED)) {
                recipients = List.of(ticket.getOwner(), ticket.getApprover());
            }

            if (!recipients.isEmpty()) {
                mailService.sendTicketHandlingEmail(recipients, ticket, TICKET_CANCELLED_SUBJECT);
            }
        } else {
            throw new TicketNotAvailableException(NOT_ENOUGH_PERMISSIONS_MSG);
        }
    }

    private void assignTicket(Ticket ticket, UserPrincipal userPrincipal) {
        if (userIsEngineer(userPrincipal) && ticket.getState().equals(State.APPROVED)) {
            var assignedUser = User.builder().id(userPrincipal.getId()).build();

            var ticketStateHistory = History.builder()
                    .user(assignedUser)
                    .ticket(ticket)
                    .action(TICKET_STATUS_CHANGED_ACTION)
                    .description(TICKET_STATUS_CHANGED_DESCRIPTION.formatted(
                            ticket.getState().name(),
                            State.IN_PROGRESS.name()))
                    .build();
            ticket.setAssignee(assignedUser);
            ticket.setState(State.IN_PROGRESS);
            ticket.getHistory().add(ticketStateHistory);
        } else {
            throw new TicketNotAvailableException(NOT_ENOUGH_PERMISSIONS_MSG);
        }
    }

    private void doneTicket(Ticket ticket, UserPrincipal userPrincipal) {
        if (userIsEngineer(userPrincipal) && ticket.getState().equals(State.IN_PROGRESS)) {
            var ticketChangeStateHistory = History.builder()
                    .user(User.builder().id(userPrincipal.getId()).build())
                    .ticket(ticket)
                    .action(TICKET_STATUS_CHANGED_ACTION)
                    .description(TICKET_STATUS_CHANGED_DESCRIPTION.formatted(
                            ticket.getState().name(),
                            State.DONE.name()))
                    .build();
            ticket.setState(State.DONE);
            ticket.getHistory().add(ticketChangeStateHistory);

            mailService.sendTicketHandlingEmail(List.of(ticket.getOwner()), ticket, TICKET_DONE_SUBJECT);
        } else {
            throw new TicketNotAvailableException(NOT_ENOUGH_PERMISSIONS_MSG);
        }
    }

    private boolean userIsManager(UserPrincipal userPrincipal) {
        var requiredAuthority = new SimpleGrantedAuthority(ROLE_PREFIX + Role.MANAGER);
        return userPrincipal.getAuthorities().contains(requiredAuthority);
    }

    private boolean userIsEngineer(UserPrincipal userPrincipal) {
        var requiredAuthority = new SimpleGrantedAuthority(ROLE_PREFIX + Role.ENGINEER);
        return userPrincipal.getAuthorities().contains(requiredAuthority);
    }

    private boolean employeeIsOwner(Ticket ticket) {
        return ticket.getOwner().getRole().equals(Role.EMPLOYEE);
    }

    private boolean userIsOwner(UserPrincipal userPrincipal, Ticket ticket) {
        return userPrincipal.getId().equals(ticket.getOwner().getId());
    }

    private boolean isNew(Ticket ticket) {
        return ticket.getState().equals(State.NEW);
    }
}
