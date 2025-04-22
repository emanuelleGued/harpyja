package com.project.harpyja.repository.nymphicus;

import com.project.harpyja.model.nymphicus.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepositoryImpl implements SessionRepository {

    private static final String COLLECTION = "sessions";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SessionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Session saveActionsToMongo(Session session) {
        return mongoTemplate.insert(session, COLLECTION);
    }

    @Override
    public void updateSessionStatusToError(String key) {
        // equivalente a UpdateOne({ key }, { $set: { status: "Error" } })
        Query query = new Query(Criteria.where("key").is(key));
        Update update = new Update().set("status", "Error");
        mongoTemplate.updateFirst(query, update, Session.class, COLLECTION);
    }
}
