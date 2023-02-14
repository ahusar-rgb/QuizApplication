package org.example.topic;

import org.example.dao.Dao;

import java.util.Optional;

public interface DaoTopic extends Dao<Topic> {
    Optional<Topic> findByName(String name);
}
