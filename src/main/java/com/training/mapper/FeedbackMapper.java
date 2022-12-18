package com.training.mapper;

import com.training.dto.feedback.InputFeedbackDto;
import com.training.dto.feedback.OutputFeedbackDto;
import com.training.entity.Feedback;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    OutputFeedbackDto convertToDto(Feedback feedback);

    Feedback convertToEntity(InputFeedbackDto inputFeedbackDto);

    List<OutputFeedbackDto> convertListToDto(List<Feedback> feedbacks);
}
