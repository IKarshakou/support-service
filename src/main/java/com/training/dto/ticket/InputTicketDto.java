package com.training.dto.ticket;

import com.training.dto.category.CategoryDto;
import com.training.dto.comment.InputCommentDto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InputTicketDto {

    private static final String TEXT_PATTERN = "[0-9a-zA-Z~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|} ]{0,500}";
    private static final String NAME_PATTERN = "[0-9a-zA-Z~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|} ]{1,500}";
    private static final String CATEGORY_VALIDATION_MSG = "The 'category' field cannot be empty.";
    private static final String NAME_VALIDATION_MSG = "The ticket's name must contain at least one English character, "
            + "digit or a special symbol and not above 100.";
    private static final String NAME_NOT_NULL_VALIDATION_MSG = "The 'name' field is missing.";
    private static final String DESCRIPTION_VALIDATION_MSG = "Description consists of English characters, "
            + "digits or a special symbols and not above 500.";
    private static final String COMMENT_VALIDATION_MSG = "Comment consists of English characters, "
            + "digits or a special symbols and not above 500.";
    private static final String URGENCY_NOT_NULL_VALIDATION_MSG = "The 'urgency' field is missing.";
    private static final String DESIRED_RESOLUTION_DATE_VALIDATION_MSG = "Desired date cannot contain past date.";

    private Long id;

    @NotNull(message = CATEGORY_VALIDATION_MSG)
    private CategoryDto category;

    @NotNull(message = NAME_NOT_NULL_VALIDATION_MSG)
    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = NAME_PATTERN,
            message = NAME_VALIDATION_MSG)
    private String name;

    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = TEXT_PATTERN,
            message = DESCRIPTION_VALIDATION_MSG)
    private String description;

    @NotNull(message = URGENCY_NOT_NULL_VALIDATION_MSG)
    private String urgency;

    @FutureOrPresent(message = DESIRED_RESOLUTION_DATE_VALIDATION_MSG)
    private LocalDate desiredResolutionDate;

    private InputCommentDto comment;
}
