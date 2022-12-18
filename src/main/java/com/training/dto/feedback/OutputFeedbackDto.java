package com.training.dto.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OutputFeedbackDto {
    private Long id;
    private Byte rate;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate date;
    private String text;
}
