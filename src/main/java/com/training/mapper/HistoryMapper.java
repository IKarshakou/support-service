package com.training.mapper;

import com.training.dto.history.OutputHistoryDto;
import com.training.entity.History;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = UserMapper.class)
public interface HistoryMapper {
    OutputHistoryDto convertToDto(History history);

    List<OutputHistoryDto> convertListToDto(List<History> histories);
}
