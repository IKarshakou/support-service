package com.training.dto.ticket;

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
public class OutputTicketDto {
    private Long id;
    private String name;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate desiredResolutionDate;
    private String urgency;
    private String state;
}
