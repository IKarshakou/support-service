package com.training.dto.ticket;

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
public class OutputTicketDto {
    private UUID id;
    private String name;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate desiredResolutionDate;
    private String urgency;
    private String state;
}
