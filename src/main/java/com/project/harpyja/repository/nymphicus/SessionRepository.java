package com.project.harpyja.repository.nymphicus;

// SessionRepository.java

import com.project.harpyja.model.nymphicus.Session;

/**
 * Abstrai o acesso à coleção "sessions" no MongoDB.
 */
public interface SessionRepository {
    /**
     * Insere um novo documento de sessão.
     *
     * @param session o objeto Session a ser salvo
     * @return a sessão salva (com eventual payload do Mongo, se aplicável)
     */
    Session saveActionsToMongo(Session session);

    /**
     * Marca a sessão identificada pela key como "Error".
     *
     * @param key o campo `key` da sessão a ser atualizada
     */
    void updateSessionStatusToError(String key);
}
