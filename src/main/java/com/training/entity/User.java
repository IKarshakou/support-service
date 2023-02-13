package com.training.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity(name = "User")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true,
            nullable = false)
    private String email;

    @ToString.Exclude
    @Column(name = "password",
            nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ToString.Exclude
    @OneToMany(mappedBy = "owner")
    private List<Ticket> myTickets = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "assignee")
    private List<Ticket> ticketsToAccomplishment = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "approver")
    private List<Ticket> ticketsToApprove = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<History> history = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Feedback> feedbacks = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<UserPrincipal> myAccounts = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Ticket> getMyTickets() {
        return myTickets;
    }

    public void setMyTickets(List<Ticket> myTickets) {
        this.myTickets = myTickets;
    }

    public List<Ticket> getTicketsToAccomplishment() {
        return ticketsToAccomplishment;
    }

    public void setTicketsToAccomplishment(List<Ticket> ticketsToAccomplishment) {
        this.ticketsToAccomplishment = ticketsToAccomplishment;
    }

    public List<Ticket> getTicketsToApprove() {
        return ticketsToApprove;
    }

    public void setTicketsToApprove(List<Ticket> ticketsToApprove) {
        this.ticketsToApprove = ticketsToApprove;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<UserPrincipal> getMyAccounts() {
        return myAccounts;
    }

    public void setMyAccounts(List<UserPrincipal> myAccounts) {
        this.myAccounts = myAccounts;
    }
}
