package com.training.dto.history;

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
public class OutputHistoryDto {
    private Long id;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private LocalDateTime date;
    private OutputUserDto user;
    private String action;
    private String description;
}
