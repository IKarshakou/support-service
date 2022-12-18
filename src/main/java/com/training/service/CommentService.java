package com.training.service;

import com.training.dto.comment.InputCommentDto;
import com.training.dto.comment.OutputCommentDto;

import java.util.List;

public interface CommentService {
    List<OutputCommentDto> findAllByTicketId(Long id);

    OutputCommentDto addCommentToTicket(Long ticketId, InputCommentDto inputCommentDto);
}
