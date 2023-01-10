package com.training.service;

import com.training.dto.attachment.AttachmentToDownloadDto;
import com.training.dto.attachment.OutputAttachmentDto;
import com.training.entity.Ticket;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AttachmentService {
    List<OutputAttachmentDto> getAttachments(UUID ticketId);

    AttachmentToDownloadDto getAttachmentToDownload(UUID attachmentId);

    void uploadAttachmentsToServer(Ticket ticket, List<MultipartFile> multipartAttachments);

    void deleteAttachment(UUID attachmentId);
}
