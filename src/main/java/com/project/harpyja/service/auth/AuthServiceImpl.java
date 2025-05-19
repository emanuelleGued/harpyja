package com.project.harpyja.service.auth;

import com.project.harpyja.dto.UserWithTokenDto;
import com.project.harpyja.exception.CustomException;
import com.project.harpyja.entity.User;
import com.project.harpyja.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserWithTokenDto login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new CustomException("Usuário não encontrado", 404);
        }

        User user = optionalUser.get();

        boolean passwordMatches = PasswordUtil.verifyPasswordMatch(password, user.getPassword());
        if (!passwordMatches) {
            throw new CustomException("Senha inválida", 401);
        }

        String token = jwtUtil.generateAuthToken(user);
        /*
        String key = userProjectService.getLastUserProjectKey(user.getId())
                .orElse(null);

         */
        return new UserWithTokenDto(user, token, null);
    }
}
