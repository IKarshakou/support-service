package com.training.dto.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OutputFeedbackDto {
    private UUID id;
    private Byte rate;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate date;
    private String text;
}
