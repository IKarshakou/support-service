package com.training.mapper;

import com.training.dto.attachment.AttachmentToDownloadDto;
import com.training.dto.attachment.OutputAttachmentDto;
import com.training.entity.Attachment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AttachmentMapper {
    OutputAttachmentDto convertToDto(Attachment attachment);

    AttachmentToDownloadDto convertToDownloadDto(Attachment attachment);

    List<OutputAttachmentDto> convertListToDto(List<Attachment> attachments);
}
