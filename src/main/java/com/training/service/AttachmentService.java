package com.training.service;

import com.training.dto.attachment.AttachmentToDownloadDto;
import com.training.dto.attachment.OutputAttachmentDto;
import com.training.entity.Ticket;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    List<OutputAttachmentDto> getAttachments(Long ticketId);

    AttachmentToDownloadDto getAttachmentToDownload(Long attachmentId);

    void uploadAttachmentsToServer(Ticket ticket, List<MultipartFile> multipartAttachments);

    void deleteAttachment(Long attachmentId);
}
