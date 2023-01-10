package com.training.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class InputUserDto {
    private static final String VALIDATION_MSG = "Please make sure you are using a valid email or password";

    @Size(max = 100, message = VALIDATION_MSG)
    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = VALIDATION_MSG)
    private String email;

    @ToString.Exclude
    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = "(?=.*[0-9])(?=.*[~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|}])"
                    + "(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|}]{6,20}",
            message = VALIDATION_MSG)
    private String password;
}
