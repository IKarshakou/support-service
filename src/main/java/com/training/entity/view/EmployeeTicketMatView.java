package com.training.entity.view;

import com.training.entity.enums.State;
import com.training.entity.enums.Urgency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Builder
@Entity
@Table(name = "employee_tickets_mat_view")
public class EmployeeTicketMatView {

    @Id
    private UUID id;

    private String ticket;
    private String category;
    private String description;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "desired_resolution_date")
    private LocalDateTime desiredResolutionDate;
    private String assignee;
    private String approver;
    private String owner;
    @Enumerated(EnumType.STRING)
    private State state;
    @Enumerated(EnumType.STRING)
    private Urgency urgency;
}
