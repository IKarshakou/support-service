package com.training.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InputCommentDto {
    @NotNull(message = "The 'text' field is missing.")
    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = "[0-9a-zA-Z~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|} ]{0,500}",
            message = "Comment consists of English characters, digits or a special symbols and not above 500.")
    private String text;
}
