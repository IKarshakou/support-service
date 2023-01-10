package com.training.dto.user;

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
public class UpdatedUserDto {

    private String firstName;
    private String lastName;

    @ToString.Exclude
    @Pattern(flags = Pattern.Flag.UNICODE_CASE,
            regexp = "(?=.*[0-9])(?=.*[~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|}])"
                    + "(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z~.\\\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|}]{6,20}",
            message = "Please make sure you are using a valid password")
    private String password;
}
