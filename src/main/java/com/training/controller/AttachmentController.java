package com.training.controller;

import com.training.dto.attachment.AttachmentToDownloadDto;
import com.training.dto.attachment.OutputAttachmentDto;
import com.training.exception.AttachmentNotFoundException;
import com.training.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AttachmentController {

    private static final String IOEXCEPTION_MSG = "Cannot read attachment.";
    private static final String HEADER_CONTENT_VALUE = "attachment; filename=";

    private final AttachmentService attachmentService;

    @GetMapping("/tickets/{id}/attachments")
    public ResponseEntity<List<OutputAttachmentDto>> getTicketAttachments(@PathVariable("id") Long ticketId) {
        return ResponseEntity.ok(attachmentService.getAttachments(ticketId));
    }

    @GetMapping("/attachments/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable("id") Long attachmentId) {
        AttachmentToDownloadDto attachment = attachmentService.getAttachmentToDownload(attachmentId);

        ByteArrayResource resource;
        try {
            resource = new ByteArrayResource(Files
                    .readAllBytes(Paths.get(attachment.getFilePath())));
        } catch (IOException ex) {
            log.error(IOEXCEPTION_MSG, ex);
            throw new AttachmentNotFoundException(ex);
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, HEADER_CONTENT_VALUE + attachment.getName())
                .body(resource);
    }

    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable("id") Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }
}
