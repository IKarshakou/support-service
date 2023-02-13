package com.training.scheduled;

import com.training.repository.EmployeeTicketMatViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EmployeeTicketMatViewRefresher {
    private final EmployeeTicketMatViewRepository viewRepository;

    @Transactional
//    @Scheduled(fixedRate = 5000L)
    public void refreshView() {
        viewRepository.refresh();
    }
}
