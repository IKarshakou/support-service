package com.training.dto.view;

import com.training.dto.user.OutputUserDto;
import com.training.entity.User;
import com.training.entity.enums.State;
import com.training.entity.enums.Urgency;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class EmployeeTicketViewDto {

    @Id
    private UUID id;

    private String ticket;
    private String category;
    private String description;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "desired_resolution_date")
    private LocalDateTime desiredResolutionDate;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private OutputUserDto assignee;
    @ToString.Exclude
    private OutputUserDto approver;
    private String owner;
    private State state;
    private Urgency urgency;
}
