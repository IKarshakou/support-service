package com.training.dto.history;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.dto.user.OutputUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OutputHistoryDto {
    private UUID id;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime date;
    private OutputUserDto user;
    private String action;
    private String description;
}
