package org.example.response;

import org.example.dao.Dao;
import org.example.question.Question;

import java.util.List;

public interface DaoResponse extends Dao<Response> {
    List<Response> findByQuestion(Question question);
}
