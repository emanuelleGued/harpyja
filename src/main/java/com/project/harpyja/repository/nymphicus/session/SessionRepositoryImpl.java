package com.project.harpyja.repository.nymphicus.session;

import com.project.harpyja.model.nymphicus.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SessionRepositoryImpl implements SessionRepository {

    private static final String COLLECTION = "sessions";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SessionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Session> findByKey(String key) {
        Query query = new Query(
                Criteria.where("key").is(key)
                        .and("status").is("Complete")
        );

        query.fields()
                .include("videourl")
                .include("activities")
                .include("device")
                .include("status")
                .include("createdAt")
                .include("key")
                .include("duration");

        return mongoTemplate.find(query, Session.class, COLLECTION);
    }

    @Override
    public Session saveActionsToMongo(Session session) {
        return mongoTemplate.insert(session, COLLECTION);
    }
}