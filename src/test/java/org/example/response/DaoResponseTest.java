package org.example.response;

import org.example.DbTest.DbTest;
import org.example.dao.DaoFactory;
import org.example.exception.NotFoundException;
import org.example.question.DaoQuestion;
import org.example.question.DifficultyRank;
import org.example.question.Question;
import org.example.topic.DaoTopic;
import org.example.topic.Topic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DaoResponseTest extends DbTest {
    private final DaoQuestion daoQuestion = DaoFactory.getInstance().getDaoQuestion();
    private final DaoResponse daoResponse = DaoFactory.getInstance().getDaoResponse();
    private final DaoTopic daoTopic = DaoFactory.getInstance().getDaoTopic();

    @Test
    void save() {
        Response expected = new Response("response", true);
        Response actual = daoResponse.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        Response initial = daoResponse.save(new Response("response", true));
        daoResponse.delete(initial);
        assertTrue(daoResponse.findById(initial.getId()).isEmpty());
    }

    @Test
    void update() {
        Response initial = daoResponse.save(new Response("response", true));
        Response expected = new Response(initial.getId(), "response", true);
        int rowsAffected = daoResponse.update(expected);

        Response actual = daoResponse.findById(initial.getId()).orElseThrow(
                () -> new NotFoundException("Response not found")
        );

        assertEquals(1, rowsAffected);
        assertEquals(expected, actual);

    }

    @Test
    void findAll() {
        Response response1 = daoResponse.save(new Response("response1", true));
        Response response2 = daoResponse.save(new Response("response2", false));
        Response response3 = daoResponse.save(new Response("response3", true));
        Response response4 = daoResponse.save(new Response("response4", false));

        assertThat(daoResponse.findAll())
                .usingRecursiveComparison()
                .isEqualTo(List.of(response1, response2, response3, response4));
    }

    @Test
    void findById() {
        Response expected = daoResponse.save(new Response("response", true));
        Response actual = daoResponse.findById(expected.getId()).orElseThrow(
                () -> new NotFoundException("Response not found")
        );
        assertEquals(expected, actual);
    }

    @Test
    void findByQuestion() {
        Topic topic = daoTopic.save(new Topic("topic"));
        Question question1 = daoQuestion.save(new Question(topic, "question", DifficultyRank.EASY));
        Question question2 = daoQuestion.save(new Question(topic, "question2", DifficultyRank.MEDIUM));

        Response response1 = daoResponse.save(new Response("response1", true));
        Response response2 = daoResponse.save(new Response("response2", false));
        Response response3 = daoResponse.save(new Response("response3", true));
        Response response4 = daoResponse.save(new Response("response4", false));

        daoQuestion.addResponse(question1, response1);
        daoQuestion.addResponse(question1, response2);
        daoQuestion.addResponse(question2, response3);
        daoQuestion.addResponse(question2, response4);

        question1 = daoQuestion.findById(question1.getId()).orElseThrow(
                () -> new NotFoundException("Question not found")
        );
        question2 = daoQuestion.findById(question2.getId()).orElseThrow(
                () -> new NotFoundException("Question not found")
        );

        assertThat(daoResponse.findByQuestion(question1))
                .usingRecursiveComparison()
                .isEqualTo(List.of(response1, response2));

        assertThat(daoResponse.findByQuestion(question2))
                .usingRecursiveComparison()
                .isEqualTo(List.of(response3, response4));
    }
}