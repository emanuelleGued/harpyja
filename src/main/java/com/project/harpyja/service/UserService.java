package com.project.harpyja.service;

import com.project.harpyja.model.User;
import com.project.harpyja.repository.UserWithProjectKey;

import java.util.List;

public interface UserService {

    /**
     * Cria um novo usuário no sistema.
     * @param user objeto contendo as informações do usuário.
     * @return o usuário criado.
     */
    User createUser(User user);

    /**
     * Define o campo 'emailVerified' como verdadeiro para o usuário associado ao e-mail informado.
     * @param email e-mail do usuário que terá o e-mail verificado.
     * @return o usuário após a atualização.
     */
    User updateEmailVerified(String email);

    /**
     * Retorna todos os usuários cadastrados.
     * @return lista de usuários.
     */
    List<User> findAllUsers();

    /**
     * Busca um usuário pelo seu ID.
     * @param userId ID em formato String (UUID) do usuário.
     * @return o usuário encontrado.
     */
    User findUserById(String userId);

    /**
     * Exclui o usuário com base no seu ID.
     * @param userId ID em formato String (UUID) do usuário.
     */
    void deleteUser(String userId);

    /**
     * Retorna uma projeção contendo informações de usuário + ProjectKey, com base no e-mail.
     * @param email e-mail do usuário.
     * @return objeto contendo o userId e o projectKey (caso exista).
     */
    UserWithProjectKey findUserWithProjectKeyByEmail(String email);

    /**
     * Verifica se já existe um usuário com o e-mail informado.
     * @param email e-mail a ser verificado.
     * @return true se o e-mail já existir, false caso contrário.
     */
    boolean emailExists(String email);

    // ==============================
    // Métodos auxiliares para Onboarding
    // ==============================

    /**
     * Verifica se o e-mail já existe no sistema. Em caso afirmativo, lança exceção para bloquear o onboarding.
     * @param email e-mail a ser verificado.
     */
    void validateUserEmailForOnboarding(String email);

    /**
     * Cria um usuário no contexto de onboarding, sem a necessidade de nome e com possíveis regras diferentes (ex. termsAgreed = false).
     * @param email e-mail do usuário.
     * @param rawPassword senha em texto plano (será futuramente criptografada).
     * @return o usuário criado.
     */
    User createUserForOnboarding(String email, String rawPassword);

    /**
     * Atualiza apenas o nome de um usuário existente, com base no ID.
     * @param userId ID do usuário.
     * @param newName novo valor para o campo "name".
     * @return o usuário atualizado.
     */
    User updateUserName(String userId, String newName);

    // ==============================
    // MÉTODOS ADICIONADOS PARA EQUIVALÊNCIA COM O GO CODE
    // ==============================

    /**
     * Atualiza parcialmente um usuário, de acordo com as propriedades em "updates".
     * Exemplo: se "updates.getName() != null", sobrescreve o campo name, etc.
     * @param userId ID do usuário a ser atualizado (UUID em string).
     * @param updates objeto contendo as alterações a aplicar.
     * @return o usuário após a atualização.
     */
    User updateUserService(String userId, User updates);

    /**
     * Verifica, para fins de onboarding, se já existe um usuário com o e-mail informado.
     * Se sim, lança exceção. Se não, retorna null (seguindo a mesma lógica do Go).
     * @param email e-mail a ser verificado.
     * @return null se o e-mail não existe, ou lança exceção caso já exista.
     */
    User findUserByEmailServiceOnboarding(String email);
}
