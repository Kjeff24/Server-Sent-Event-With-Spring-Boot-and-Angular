package com.bexos.backend.services;

import com.bexos.backend.entitites.User;

public interface UserService {
    User getAuthenticatedUser();
}
