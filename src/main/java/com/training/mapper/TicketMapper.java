package com.training.mapper;

import com.training.dto.ticket.InputDraftTicketDto;
import com.training.dto.ticket.InputTicketDto;
import com.training.dto.ticket.OutputTicketDto;
import com.training.dto.ticket.OutputTicketWithDetailsDto;
import com.training.entity.enums.Urgency;
import com.training.entity.Category;
import com.training.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(imports = {CategoryMapper.class, UserMapper.class, CommentMapper.class, Urgency.class, Category.class})
public interface TicketMapper {

    OutputTicketDto convertToDto(Ticket ticket);

    @Mapping(target = "comments",
            expression = "java(new CommentMapperImpl().convertToEntityList(inputTicketDto.getComment()))")
    @Mapping(target = "urgency",
            expression = "java(Enum.valueOf(Urgency.class, inputTicketDto.getUrgency().toUpperCase()))")
    Ticket convertToEntity(InputTicketDto inputTicketDto);

    @Mapping(target = "comments",
            expression = "java(new CommentMapperImpl().convertToEntityList(inputDraftTicketDto.getComment()))")
    @Mapping(target = "urgency",
            expression = "java((inputDraftTicketDto.getUrgency() != null) "
                    + "? Enum.valueOf(Urgency.class, inputDraftTicketDto.getUrgency().toUpperCase())"
                    + ": Urgency.LOW)")
    @Mapping(target = "name", defaultValue = "Ticket Draft")
    Ticket convertDraftToEntity(InputDraftTicketDto inputDraftTicketDto);

    List<OutputTicketDto> convertListToDto(List<Ticket> tickets);

    OutputTicketWithDetailsDto convertToTicketWithDetailsDto(Ticket ticket);
}
