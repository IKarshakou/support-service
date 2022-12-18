package com.training.mapper;

import com.training.dto.history.OutputHistoryDto;
import com.training.entity.History;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface HistoryMapper {
    OutputHistoryDto convertToDto(History history);

    History convertToEntity(OutputHistoryDto outputHistoryDto);

    List<OutputHistoryDto> convertListToDto(List<History> histories);

    List<History> convertListToEntity(List<OutputHistoryDto> historiesDto);
}
