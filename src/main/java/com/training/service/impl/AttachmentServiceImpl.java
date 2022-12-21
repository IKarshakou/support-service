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
import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService, InitializingBean {

    @Value("${upload.path}")
    private String uploadPath;

    private static final String ATTACHMENT_NOT_FOUND_MSG = "Attachment not found.";
    private static final String CANNOT_DELETE_ATTACHMENT_LOG = "Cannot delete attachment. Attachment Not found.";
    private static final String FILE_ATTACHED_ACTION = "File is attached.";
    private static final String DOT_SEPARATOR = ".";
    private static final String FILE_ATTACHED_DESCRIPTION = "File is attached: [%s]";
    private static final String FILE_REMOVED_ACTION = "File is removed.";
    private static final String FILE_REMOVED_DESCRIPTION = "File is removed: [%s]";

    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    @Override
    public void afterPropertiesSet() {
        var uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OutputAttachmentDto> getAttachments(Long ticketId) {
        var attachmentList = attachmentRepository.getAttachmentsByTicketId(ticketId);
        return attachmentMapper.convertListToDto(attachmentList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AttachmentToDownloadDto getAttachmentToDownload(Long attachmentId) {
        var attachment = attachmentRepository
                .findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException(ATTACHMENT_NOT_FOUND_MSG));

        return attachmentMapper.convertToDownloadDto(attachment);
    }

    @Override
    @Transactional
    public void uploadAttachmentsToServer(Ticket ticket, List<MultipartFile> multipartAttachments) {
        if (multipartAttachments != null) {
            var attachmentList = new ArrayList<Attachment>();
            for (MultipartFile file : multipartAttachments) {
                if ((file.getOriginalFilename() != null) && !file.getOriginalFilename().isEmpty()) {
                    var originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
                    var uuidFile = UUID.randomUUID().toString();
                    var resultFilename = uuidFile + DOT_SEPARATOR + originalFileName;
                    var uploadRootPath = Paths.get(uploadPath);

                    try {
                        file.transferTo(uploadRootPath.resolve(resultFilename));
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                        throw new AttachmentUploadException(ex);
                    }

                    var attachment = Attachment.builder()
                            .filePath(uploadRootPath.resolve(resultFilename).toString())
                            .ticket(ticket)
                            .name(originalFileName)
                            .build();
                    attachmentList.add(attachment);

                    addAttachHistoryToTicket(ticket, originalFileName);
                }
            }
            ticket.setAttachments(attachmentList);
        }
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        var attachment = attachmentRepository
                .findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException(ATTACHMENT_NOT_FOUND_MSG));

        try {
            Files.deleteIfExists(Paths.get(attachment.getFilePath()));
            addAttachRemovalHistoryToTicket(attachment.getTicket(), attachment.getName());
        } catch (IOException ex) {
            log.error(CANNOT_DELETE_ATTACHMENT_LOG, ex);
            throw new AttachmentNotFoundException(ex);
        }

        attachmentRepository.delete(attachment);
    }

    private void addAttachHistoryToTicket(Ticket ticket, String filename) {
        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        var historyElement = History.builder()
                .ticket(ticket)
                .user(User.builder().id(userPrincipal.getId()).build())
                .action(FILE_ATTACHED_ACTION)
                .description(FILE_ATTACHED_DESCRIPTION.formatted(filename))
                .build();

        ticket.getHistory().add(historyElement);
    }

    private void addAttachRemovalHistoryToTicket(Ticket ticket, String filename) {
        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        var historyElement = History.builder()
                .ticket(ticket)
                .user(User.builder().id(userPrincipal.getId()).build())
                .action(FILE_REMOVED_ACTION)
                .description(FILE_REMOVED_DESCRIPTION.formatted(filename))
                .build();

        ticket.getHistory().add(historyElement);
    }
}
