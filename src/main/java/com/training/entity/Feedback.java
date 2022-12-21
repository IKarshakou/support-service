package com.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Entity(name = "Feedback")
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id",
            updatable = false)
    private Long id;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false)
    private User user;

    @Setter
    @Column(name = "feedback_rate",
            nullable = false)
    private Byte rate;

    @CreationTimestamp
    @Column(name = "feedback_date",
            updatable = false,
            nullable = false)
    private LocalDate date;

    @Setter
    @Column(name = "feedback_text")
    private String text;

    @ToString.Exclude
    @Setter
    @OneToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id",
            nullable = false,
            unique = true)
    private Ticket ticket;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(rate, feedback.rate)
                && Objects.equals(date, feedback.date)
                && Objects.equals(text, feedback.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rate, date, text);
    }
}
