package org.example.dao;

import org.example.question.DaoQuestion;
import org.example.question.DaoQuestionImpl;
import org.example.response.DaoResponse;
import org.example.response.DaoResponseImpl;
import org.example.topic.DaoTopic;
import org.example.topic.DaoTopicImpl;

public class DaoFactory {
    private static DaoFactory daoFactory;
    static {
        daoFactory = new DaoFactory();
    }

    private DaoFactory() {}

    public static DaoFactory getInstance() {
        return daoFactory;
    }

    public DaoQuestion getDaoQuestion() {
        return new DaoQuestionImpl();
    }

    public DaoResponse getDaoResponse() {
        return new DaoResponseImpl();
    }

    public DaoTopic getDaoTopic() {
        return new DaoTopicImpl();
    }
}
