package com.project.harpyja.repository.nymphicus.session;

import com.project.harpyja.model.nymphicus.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository {

    List<Session> findByKey(String key);

    Session saveActionsToMongo(Session session);

    void updateSessionStatusToError(String key);
}