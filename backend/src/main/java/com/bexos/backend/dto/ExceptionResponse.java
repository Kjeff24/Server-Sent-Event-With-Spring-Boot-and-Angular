package com.bexos.backend.dto;

import lombok.Builder;

@Builder
public record ExceptionResponse(
        String message,
        int status
) {
}
