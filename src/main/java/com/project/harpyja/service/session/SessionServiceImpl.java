package com.project.harpyja.service.session;

import com.project.harpyja.model.nymphicus.Session;
import com.project.harpyja.repository.nymphicus.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Session> getSessionByKey(String key) {
        return sessionRepository.findByKey(key);
    }
}