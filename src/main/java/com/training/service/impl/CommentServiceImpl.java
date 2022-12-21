package com.training.service.impl;

import com.training.dto.comment.InputCommentDto;
import com.training.dto.comment.OutputCommentDto;
import com.training.entity.Ticket;
import com.training.mapper.CommentMapper;
import com.training.repository.CommentRepository;
import com.training.repository.UserRepository;
import com.training.security.UserPrincipal;
import com.training.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String COMMENTS_NOT_FOUND_MSG = "This ticket has no comments.";
    private static final String USER_NOT_FOUND = "Who are you? How did you get here?";

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OutputCommentDto> findAllByTicketId(Long id) {
        var commentList = commentRepository.findAllByTicketId(id);

        if (commentList.isEmpty()) {
            throw new EntityNotFoundException(COMMENTS_NOT_FOUND_MSG);
        }

        return commentMapper.convertListToDto(commentList);
    }

    @Override
    @Transactional
    public OutputCommentDto addCommentToTicket(Long ticketId, InputCommentDto inputCommentDto) {
        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        var user = userRepository
                .findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        var ticket = Ticket.builder()
                .id(ticketId)
                .build();

        var comment = commentMapper.convertToEntity(inputCommentDto);
        comment.setUser(user);
        comment.setTicket(ticket);

        return commentMapper.convertToDto(commentRepository.save(comment));
    }
}
