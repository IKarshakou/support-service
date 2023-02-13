package com.training.entity;

import com.training.entity.enums.State;
import com.training.entity.enums.Urgency;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Entity(name = "Ticket")
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
            )
    })
    @Column(updatable = false)
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    private String description;

    @CreationTimestamp
    @Column(name = "created_on",
            updatable = false,
            nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "desired_resolution_date")
    private LocalDateTime desiredResolutionDate;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Urgency urgency;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<History> history = new ArrayList<>();

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(name, ticket.name)
                && Objects.equals(description, ticket.description)
                && Objects.equals(creationDate, ticket.creationDate)
                && Objects.equals(desiredResolutionDate, ticket.desiredResolutionDate)
                && state == ticket.state
                && urgency == ticket.urgency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, creationDate, desiredResolutionDate, state, urgency);
    }
}
