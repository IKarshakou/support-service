package com.training.entity;

import com.training.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Entity(name = "User")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id",
            updatable = false)
    private UUID id;

    @Setter
    @Column(name = "first_name")
    private String firstName;

    @Setter
    @Column(name = "last_name")
    private String lastName;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "role_id",
            nullable = false)
    private Role role;

    @Setter
    @Column(name = "user_email",
            unique = true,
            nullable = false)
    private String email;

    @ToString.Exclude
    @Setter
    @Column(name = "user_password",
            nullable = false)
    private String password;

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "owner")
    private List<Ticket> myTickets = new ArrayList<>();

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "assignee")
    private List<Ticket> ticketsToAccomplishment = new ArrayList<>();

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "approver")
    private List<Ticket> ticketsToApprove = new ArrayList<>();

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "user")
    private List<History> history = new ArrayList<>();

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @Setter
    @OneToMany(mappedBy = "user")
    private List<Feedback> feedbacks = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && role == user.role
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, role, email, password);
    }
}
