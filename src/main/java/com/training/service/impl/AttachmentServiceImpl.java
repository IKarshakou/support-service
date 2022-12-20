package com.training.service.impl;

import com.training.dto.attachment.AttachmentToDownloadDto;
import com.training.dto.attachment.OutputAttachmentDto;
import com.training.entity.Attachment;
import com.training.entity.History;
import com.training.entity.Ticket;
import com.training.entity.User;
import com.training.exception.AttachmentNotFoundException;
import com.training.exception.AttachmentUploadException;
import com.training.mapper.AttachmentMapper;
import com.training.repository.AttachmentRepository;
import com.training.security.UserPrincipal;
import com.training.service.AttachmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    @Value("${upload.path}")
    private String uploadPath;

    private final Path uploadRoot = Paths.get("src/main/resources/attachments/attachments");

    private static final String ATTACHMENT_NOT_FOUND_MSG = "Attachment not found.";
    private static final String CANNOT_DELETE_ATTACHMENT_LOG = "Cannot delete attachment. Attachment Not found.\n {}";
    private static final String FILE_ATTACHED_ACTION = "File is attached.";
    private static final String DOT_SEPARATOR = ".";
    private static final String FILE_ATTACHED_DESCRIPTION = "File is attached: [%s]";
    private static final String FILE_REMOVED_ACTION = "File is removed.";
    private static final String FILE_REMOVED_DESCRIPTION = "File is removed: [%s]";

    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    @Override
    public void afterPropertiesSet() {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputAttachmentDto> getAttachments(Long ticketId) {
        List<Attachment> attachments = attachmentRepository.getAttachmentsByTicketId(ticketId);
        return attachmentMapper.convertListToDto(attachments);
    }

    @Override
    @Transactional(readOnly = true)
    public AttachmentToDownloadDto getAttachmentToDownload(Long attachmentId) {
        Attachment attachment = attachmentRepository
                .findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException(ATTACHMENT_NOT_FOUND_MSG));

        return attachmentMapper.convertToDownloadDto(attachment);
    }

    @Override
    @Transactional
    public void uploadAttachmentsToServer(Ticket ticket, List<MultipartFile> multipartAttachments) {
        if (multipartAttachments != null) {
            List<Attachment> attachments = new ArrayList<>();
            for (MultipartFile file : multipartAttachments) {
                if ((file.getOriginalFilename() != null) && !file.getOriginalFilename().isEmpty()) {
                    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile + DOT_SEPARATOR + originalFileName;

                    try {
                        file.transferTo(uploadRoot.resolve(resultFilename));
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                        throw new AttachmentUploadException(ex);
                    }

                    Attachment attachment = Attachment.builder()
                            .filePath(uploadRoot.resolve(resultFilename).toString())
                            .ticket(ticket)
                            .name(originalFileName)
                            .build();
                    attachments.add(attachment);

                    addAttachHistoryToTicket(ticket, originalFileName);
                }
            }
            ticket.setAttachments(attachments);
        }
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository
                .findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException(ATTACHMENT_NOT_FOUND_MSG));

        try {
            Files.deleteIfExists(Paths.get(attachment.getFilePath()));
            addAttachRemovalHistoryToTicket(attachment.getTicket(), attachment.getName());
        } catch (IOException ex) {
            log.error(CANNOT_DELETE_ATTACHMENT_LOG, ex.getMessage());
            throw new AttachmentNotFoundException(ex);
        }

        attachmentRepository.delete(attachment);
    }

    private void addAttachHistoryToTicket(Ticket ticket, String filename) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        History fileAttachHistory = History.builder()
                .ticket(ticket)
                .user(User.builder().id(userPrincipal.getId()).build())
                .action(FILE_ATTACHED_ACTION)
                .description(FILE_ATTACHED_DESCRIPTION.formatted(filename))
                .build();

        ticket.getHistory().add(fileAttachHistory);
    }

    private void addAttachRemovalHistoryToTicket(Ticket ticket, String filename) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        History fileRemoveHistory = History.builder()
                .ticket(ticket)
                .user(User.builder().id(userPrincipal.getId()).build())
                .action(FILE_REMOVED_ACTION)
                .description(FILE_REMOVED_DESCRIPTION.formatted(filename))
                .build();

        ticket.getHistory().add(fileRemoveHistory);
    }
}
