package com.training.service.impl;

import com.training.dto.comment.InputCommentDto;
import com.training.dto.comment.OutputCommentDto;
import com.training.entity.Comment;
import com.training.entity.Ticket;
import com.training.entity.User;
import com.training.mapper.CommentMapper;
import com.training.repository.CommentRepository;
import com.training.repository.UserRepository;
import com.training.security.UserPrincipal;
import com.training.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private static final String COMMENTS_NOT_FOUND_MSG = "This ticket has no comments.";
    private static final String USER_NOT_FOUND = "Who are you? How did you get here?";

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OutputCommentDto> findAllByTicketId(Long id) {
        List<Comment> comments = commentRepository.findAllByTicketId(id);

        if (comments.isEmpty()) {
            throw new EntityNotFoundException(COMMENTS_NOT_FOUND_MSG);
        }

        return commentMapper.convertListToDto(comments);
    }

    @Override
    @Transactional
    public OutputCommentDto addCommentToTicket(Long ticketId, InputCommentDto inputCommentDto) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository
                .findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .build();

        Comment comment = commentMapper.convertToEntity(inputCommentDto);
        comment.setUser(user);
        comment.setTicket(ticket);

        return commentMapper.convertToDto(commentRepository.save(comment));
    }
}
