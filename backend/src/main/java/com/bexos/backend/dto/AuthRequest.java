package com.bexos.backend.dto;

public record AuthRequest(
        String email,
        String password
) {}
