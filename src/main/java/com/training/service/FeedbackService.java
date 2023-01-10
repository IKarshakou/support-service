package com.training.service;

import com.training.dto.feedback.InputFeedbackDto;
import com.training.dto.feedback.OutputFeedbackDto;

import java.util.UUID;

public interface FeedbackService {

    void addFeedback(UUID ticketId, InputFeedbackDto inputFeedbackDto);

    OutputFeedbackDto getFeedback(UUID ticketId);
}
