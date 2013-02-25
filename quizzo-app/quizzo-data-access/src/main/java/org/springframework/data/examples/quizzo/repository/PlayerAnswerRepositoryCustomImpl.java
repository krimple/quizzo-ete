package org.springframework.data.examples.quizzo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.PlayerAnswer;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.query;

public class PlayerAnswerRepositoryCustomImpl implements PlayerAnswerRepositoryCustom {

    @Autowired
    public void setMongoOperations(MongoDbFactory mongo) {
        this.mongoTemplate = new MongoTemplate(mongo);
    }

    private MongoTemplate mongoTemplate;

    @Override
    public int calculateScore(String gameId, String playerName) {
        List<PlayerAnswer> scores = mongoTemplate.find(
                query(
                        where("gameId").is(gameId)
                                .and("playerId").is(playerName)
                ),
                PlayerAnswer.class);

        int score = 0;
        for (PlayerAnswer answer : scores) {
            score += answer.getScore();
        }

        return score;
    }
}
