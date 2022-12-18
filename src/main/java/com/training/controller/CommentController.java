package com.training.controller;

import com.training.dto.comment.InputCommentDto;
import com.training.dto.comment.OutputCommentDto;
import com.training.service.CommentService;
import com.training.service.ErrorsHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tickets/{id}/comments")
@RequiredArgsConstructor
public class CommentController {

    private static final String COMMENTS_PATH = "/comments";
    private static final String SLASH = "/";

    private final CommentService commentService;
    private final ErrorsHandlerService errorsHandlerService;

    @GetMapping
    public ResponseEntity<List<OutputCommentDto>> getTicketComments(@PathVariable("id") Long id) {
        return ResponseEntity.ok(commentService.findAllByTicketId(id));
    }

    @PostMapping
    public ResponseEntity<OutputCommentDto> addComment(@PathVariable("id") Long ticketId,
                                                       @Validated @RequestBody InputCommentDto inputCommentDto,
                                                       Errors errors) {
        errorsHandlerService.checkErrors(errors);
        return ResponseEntity
                .created(URI.create(SLASH + ticketId + COMMENTS_PATH))
                .body(commentService.addCommentToTicket(ticketId, inputCommentDto));
    }
}
