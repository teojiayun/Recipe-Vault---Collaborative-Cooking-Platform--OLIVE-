package com.recipevault.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String username;
    private String fullName;
    private String token;
}
