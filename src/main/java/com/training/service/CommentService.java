package com.training.service;

import com.training.dto.comment.InputCommentDto;
import com.training.dto.comment.OutputCommentDto;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    List<OutputCommentDto> findAllByTicketId(UUID id);

    OutputCommentDto addCommentToTicket(UUID ticketId, InputCommentDto inputCommentDto);
}
