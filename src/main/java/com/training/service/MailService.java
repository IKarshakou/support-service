package com.training.service;

import com.training.entity.Ticket;
import com.training.entity.User;

import java.util.List;

public interface MailService {
    void sendTicketHandlingEmail(List<User> recipients, Ticket ticket, String subject);
}
