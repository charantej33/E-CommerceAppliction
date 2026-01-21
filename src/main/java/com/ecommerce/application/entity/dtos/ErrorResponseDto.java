package com.ecommerce.application.entity.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorResponseDto {
    private int statusCode;
    private String message;
    private String error;
    private LocalDateTime timestamp;
    private String path;
}
