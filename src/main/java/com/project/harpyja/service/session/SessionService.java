package com.project.harpyja.service.session;

import com.project.harpyja.model.nymphicus.Session;

import java.util.List;

public interface SessionService {
    List<Session> getSessionByKey(String key);
}