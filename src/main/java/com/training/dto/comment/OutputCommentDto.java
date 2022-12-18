package com.training.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.dto.user.OutputUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OutputCommentDto {
    private Long id;
    private OutputUserDto user;
    private String text;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private LocalDateTime creationDate;
}
