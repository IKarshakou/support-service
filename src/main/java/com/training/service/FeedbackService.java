package com.training.service;

import com.training.dto.feedback.InputFeedbackDto;
import com.training.dto.feedback.OutputFeedbackDto;

public interface FeedbackService {

    void addFeedback(Long ticketId, InputFeedbackDto inputFeedbackDto);

    OutputFeedbackDto getFeedback(Long ticketId);
}
