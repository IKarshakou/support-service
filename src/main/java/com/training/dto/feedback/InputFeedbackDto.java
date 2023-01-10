package com.training.dto.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class InputFeedbackDto {

    private static final String INCOMPATIBLE_RATE = "Incompatible rate. Can be from 1 to 5.";

    @Min(value = 1, message = INCOMPATIBLE_RATE)
    @Max(value = 5, message = INCOMPATIBLE_RATE)
    @NotNull(message = "Rating is required.")
    private Byte rate;

    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = "[0-9a-zA-Z~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|} ]{0,500}",
            message = "Feedback consists of English characters, digits or a special symbols and not above 500.")
    private String text;
}
