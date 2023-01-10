package com.training.specification;

import com.training.entity.Ticket;
import com.training.entity.enums.State;
import com.training.entity.enums.Urgency;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class TicketSpecification {

    private static final String TICKET_ID = "id";
    private static final String TICKET_NAME = "name";
    private static final String TICKET_DESIRED_DATE = "desiredResolutionDate";
    private static final String TICKET_URGENCY = "urgency";
    private static final String TICKET_STATE = "state";
    private static final String MATCHING_STRING = "%%%s%%";

    public static Specification<Ticket> idContains(UUID id) {
        return ((root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get(TICKET_ID), MATCHING_STRING.formatted(id.toString())));
    }

    public static Specification<Ticket> nameContains(String name) {
        return ((root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get(TICKET_NAME), MATCHING_STRING.formatted(name)));
    }

    public static Specification<Ticket> hasDesiredDate(LocalDate desiredDate) {
        return ((root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(TICKET_DESIRED_DATE), desiredDate));
    }

    public static Specification<Ticket> hasUrgency(Urgency urgency) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(TICKET_URGENCY), urgency));
    }

    public static Specification<Ticket> hasState(State state) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(TICKET_STATE), state));
    }
}
