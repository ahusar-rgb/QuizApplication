package org.example.question;

import org.example.dao.Dao;
import org.example.response.Response;
import org.example.topic.Topic;

import java.util.List;

public interface DaoQuestion extends Dao<Question> {
    List<Question> findByTopic(Topic topic);
    void addResponse(Question question, Response responseDto);
    void removeResponse(Question question, Response response);
}
