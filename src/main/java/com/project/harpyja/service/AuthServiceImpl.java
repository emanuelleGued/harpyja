package com.project.harpyja.service;

import com.project.harpyja.dto.UserWithTokenDto;
import com.project.harpyja.exception.CustomException;
import com.project.harpyja.entity.User;
import com.project.harpyja.repository.UserRepository;
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
        // Busca o usuário pelo email
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new CustomException("Usuário não encontrado", 404);
        }

        User user = optionalUser.get();

        // Compara a senha (exemplo simples; ideal seria comparar hash com BCrypt)
        if (!user.comparePasswords(password)) {
            throw new CustomException("Credenciais inválidas", 401);
        }

        // Limpa a senha para não retornar em JSON
        user.sanitizePassword();

        // Gera o token JWT
        String token = jwtUtil.generateToken(user.getId().toString());

        // Exemplo: se você tiver projectKey, busque/adicione aqui
        String projectKey = "projectKeyExemplo"; // Ajuste conforme sua lógica real

        // Monta objeto de retorno
        return new UserWithTokenDto(user, token, projectKey);
    }
}
