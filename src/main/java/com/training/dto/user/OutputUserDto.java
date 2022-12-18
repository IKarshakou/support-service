package com.training.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OutputUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    private String email;
}
