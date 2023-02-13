package com.training.controller;

import com.training.dto.view.EmployeeTicketViewDto;
import com.training.entity.view.EmployeeTicketMatView;
import com.training.entity.view.EmployeeTicketView;
import com.training.entity.view.ManagerTicketMatView;
import com.training.entity.view.ManagerTicketView;
import com.training.service.EmployeeTicketMatViewService;
import com.training.service.EmployeeTicketViewService;
import com.training.service.ManagerTicketMatViewService;
import com.training.service.ManagerTicketViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestViewController {

    private final EmployeeTicketMatViewService employeeTicketMatViewService;
    private final ManagerTicketMatViewService managerTicketMatViewService;
    private final EmployeeTicketViewService employeeTicketViewService;
    private final ManagerTicketViewService managerTicketViewService;

    @GetMapping("/tickets/employees")
    public ResponseEntity<List<EmployeeTicketMatView>> getEmployeeTicketsMatView() {
        return ResponseEntity.ok(employeeTicketMatViewService.findAllEmployeeTickets());
    }

    @GetMapping("/tickets/managers")
    public ResponseEntity<List<ManagerTicketMatView>> getManagerTicketsMatView() {
        return ResponseEntity.ok(managerTicketMatViewService.findAllManagerTickets());
    }

    @GetMapping("/tickets/employee")
    public ResponseEntity<List<EmployeeTicketViewDto>> getEmployeeTicketsView() {
        return ResponseEntity.ok(employeeTicketViewService.findAllEmployeeTickets());
    }

    @GetMapping("/tickets/manager")
    public ResponseEntity<List<ManagerTicketView>> getManagerTicketsView() {
        return ResponseEntity.ok(managerTicketViewService.findAllManagerTickets());
    }
}
