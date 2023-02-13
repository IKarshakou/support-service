package com.training.mapper;

import com.training.dto.view.EmployeeTicketViewDto;
import com.training.entity.view.EmployeeTicketView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = UserMapper.class)
public interface ViewMapper {
    List<EmployeeTicketViewDto> toDto(List<EmployeeTicketView> tickets);
}
