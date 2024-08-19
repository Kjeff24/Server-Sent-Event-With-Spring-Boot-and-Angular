package com.bexos.backend.services;

import com.bexos.backend.dto.AuthRequest;
import com.bexos.backend.dto.AuthenticationResponse;
import com.bexos.backend.dto.RegisterRequest;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthRequest request);
}
