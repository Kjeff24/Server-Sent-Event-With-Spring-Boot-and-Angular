package com.bexos.backend.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}
