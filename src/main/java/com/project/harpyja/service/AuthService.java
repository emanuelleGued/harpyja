package com.project.harpyja.service;


import com.project.harpyja.dto.UserWithTokenDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    UserWithTokenDto login(String email, String password);
}
