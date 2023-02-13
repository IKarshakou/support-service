package com.training.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.dto.attachment.OutputAttachmentDto;
import com.training.dto.category.CategoryDto;
import com.training.dto.comment.OutputCommentDto;
import com.training.dto.history.OutputHistoryDto;
import com.training.dto.user.OutputUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OutputTicketWithDetailsDto {
    private String name;
    @JsonFormat(pattern="dd.MM.yyyy hh:mm:ss")
    private LocalDateTime creationDate;
    private String state;
    private CategoryDto category;
    private String urgency;
    private String description;
    @JsonFormat(pattern="dd.MM.yyyy hh:mm:ss")
    private LocalDateTime desiredResolutionDate;
    private OutputUserDto owner;
    private OutputUserDto approver;
    private OutputUserDto assignee;
    private List<OutputAttachmentDto> attachments;
    private List<OutputCommentDto> comments;
    private List<OutputHistoryDto> history;
}
