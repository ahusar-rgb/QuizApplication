package org.example.topic;

import org.example.DbTest.DbTest;
import org.example.dao.DaoFactory;
import org.example.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DaoTopicImplTest extends DbTest {
    private final DaoTopic daoTopic = DaoFactory.getInstance().getDaoTopic();
    @Test
    void save() {
        Topic expected = new Topic("topic");
        Topic actual = daoTopic.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        Topic initial = daoTopic.save(new Topic("topic"));
        daoTopic.delete(initial);
        assertTrue(daoTopic.findById(initial.getId()).isEmpty());
    }

    @Test
    void update() {
        Topic initial = daoTopic.save(new Topic("topic"));
        Topic expected = new Topic(initial.getId(), "topic");
        int rowsAffected = daoTopic.update(expected);

        Topic actual = daoTopic.findById(initial.getId()).orElseThrow(
                () -> new NotFoundException("Topic not found")
        );

        assertEquals(1, rowsAffected);
        assertEquals(expected, actual);
    }

    @Test
    void findAll() {
        Topic topic1 = daoTopic.save(new Topic("topic1"));
        Topic topic2 = daoTopic.save(new Topic("topic2"));
        Topic topic3 = daoTopic.save(new Topic("topic3"));
        Topic topic4 = daoTopic.save(new Topic("topic4"));

        assertThat(daoTopic.findAll())
                .usingRecursiveComparison()
                .isEqualTo(List.of(topic1, topic2, topic3, topic4));
    }

    @Test
    void findById() {
        Topic expected = daoTopic.save(new Topic("topic"));
        Topic actual = daoTopic.findById(expected.getId()).orElseThrow(
                () -> new NotFoundException("Topic not found")
        );
        assertEquals(expected, actual);
    }

    @Test
    void findByName() {
        Topic expected = daoTopic.save(new Topic("topic"));
        Topic actual = daoTopic.findByName(expected.getName()).orElseThrow(
                () -> new NotFoundException("Topic not found")
        );
        assertEquals(expected, actual);
    }
}