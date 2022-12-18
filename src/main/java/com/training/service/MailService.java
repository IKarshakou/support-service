package com.training.service;

import com.training.entity.User;

import java.util.List;

public interface MailService {
    void sendTicketHandlingEmail(List<User> recipients, Long ticketId, String subject);
}
