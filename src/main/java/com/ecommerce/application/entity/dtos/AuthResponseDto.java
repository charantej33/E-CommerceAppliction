package com.ecommerce.application.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthResponseDto {
    private String token;
    private String type;
    private Long id;
    private String email;
    private String name;
    private String role;

    public AuthResponseDto(String token) {
        this.token = token;
        this.type = "Bearer";
    }
}
