package com.training.mapper;

import com.training.dto.comment.InputCommentDto;
import com.training.dto.ticket.InputDraftTicketDto;
import com.training.dto.ticket.InputTicketDto;
import com.training.dto.ticket.OutputTicketDto;
import com.training.dto.ticket.OutputTicketWithDetailsDto;
import com.training.entity.Ticket;
import com.training.entity.enums.Urgency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(imports = {CategoryMapper.class, UserMapper.class, CommentMapper.class, Urgency.class})
public interface TicketMapper {

    OutputTicketDto convertToDto(Ticket ticket);

    @Mapping(target = "urgency",
            expression = "java(Enum.valueOf(Urgency.class, inputTicketDto.getUrgency().toUpperCase()))")
    @Mapping(target = "comments", source = "inputCommentsDto")
    @Mapping(target = "history", expression = "java(new ArrayList<>())")
    Ticket convertToEntity(InputTicketDto inputTicketDto, List<InputCommentDto> inputCommentsDto);

    @Mapping(target = "urgency",
            expression = "java((inputDraftTicketDto.getUrgency() != null) "
                    + "? Enum.valueOf(Urgency.class, inputDraftTicketDto.getUrgency().toUpperCase())"
                    + ": Urgency.LOW)")
    @Mapping(target = "name", defaultValue = "Ticket Draft")
    @Mapping(target = "comments", source = "inputCommentsDto")
    @Mapping(target = "history", expression = "java(new ArrayList<>())")
    Ticket convertDraftToEntity(InputDraftTicketDto inputDraftTicketDto, List<InputCommentDto> inputCommentsDto);

    List<OutputTicketDto> convertListToDto(List<Ticket> tickets);

    OutputTicketWithDetailsDto convertToTicketWithDetailsDto(Ticket ticket);
}
