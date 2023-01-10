package com.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Entity(name = "Attachment")
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue
    @Column(name = "attachment_id",
            updatable = false)
    private UUID id;

    @ToString.Exclude
    @Setter
    @Column(name = "attachment_blob",
            unique = true,
            nullable = false)
    private String filePath;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id",
            nullable = false)
    private Ticket ticket;

    @Setter
    @Column(name = "attachment_name",
            nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(filePath, that.filePath)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, name);
    }
}
