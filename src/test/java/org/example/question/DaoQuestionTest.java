package org.example.question;

import org.example.DbTest.DbTest;
import org.example.dao.DaoFactory;
import org.example.exception.NotFoundException;
import org.example.response.Response;
import org.example.topic.DaoTopic;
import org.example.topic.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class DaoQuestionTest extends DbTest {
    private final DaoQuestion daoQuestion = DaoFactory.getInstance().getDaoQuestion();
    private final DaoTopic daoTopic = DaoFactory.getInstance().getDaoTopic();
    @Test
    @DisplayName("It should find questions by topic")
    public void itShouldFindQuestionsByTopic() {
        Topic topicJava = daoTopic.save(new Topic("Java"));
        Topic topicSchool = daoTopic.save(new Topic("School"));

        Question question1 = daoQuestion.save(new Question(topicJava, "What is Java?", DifficultyRank.EASY));
        daoQuestion.addResponse(question1, new Response("Java is a programming language", true));
        daoQuestion.addResponse(question1, new Response("Java is a technology", true));
        daoQuestion.addResponse(question1, new Response("It is a car", false));
        daoQuestion.addResponse(question1, new Response("It is a book", false));

        Question question2 = daoQuestion.save(new Question(topicJava, "What is a class?", DifficultyRank.MEDIUM));
        daoQuestion.addResponse(question2, new Response("A class is a blueprint for creating objects", true));
        daoQuestion.addResponse(question2, new Response("A class is a type of object", true));
        daoQuestion.addResponse(question2, new Response("A class is a type of car", false));

        Question question3 = daoQuestion.save(new Question(topicJava, "What is an interface?", DifficultyRank.HARD));
        daoQuestion.addResponse(question3, new Response("An interface is a blueprint for creating objects", true));
        daoQuestion.addResponse(question3, new Response("An interface is a type of object", true));
        daoQuestion.addResponse(question3, new Response("An interface is a type of coffee", false));

        Question question4 = daoQuestion.save(new Question(topicSchool, "What is a school?", DifficultyRank.EASY));
        daoQuestion.addResponse(question4, new Response("A school is a place where you learn", true));
        daoQuestion.addResponse(question4, new Response("A school is a place to work out", false));

        Question question5 = daoQuestion.save(new Question(topicSchool, "Who is a teacher?", DifficultyRank.MEDIUM));
        daoQuestion.addResponse(question5, new Response("A teacher is a person who teaches", true));
        daoQuestion.addResponse(question5, new Response("A teacher is a person who studies in school", false));

        List<Question> questionsAboutJava = daoQuestion.findByTopic(topicJava);
        assertThat(questionsAboutJava).usingRecursiveComparison()
                .ignoringFields("responseList")
                .isEqualTo(List.of(question1, question2, question3));

        List<Question> questionsAboutSchool = daoQuestion.findByTopic(topicSchool);
        assertThat(questionsAboutSchool).usingRecursiveComparison()
                .ignoringFields("responseList")
                .isEqualTo(List.of(question4, question5));
    }

    @Test
    @DisplayName("It should add a response to a question")
    public void itShouldAddAResponseToAQuestion() {
        Topic topicJava = daoTopic.save(new Topic("Java"));
        Question question1 = daoQuestion.save(new Question(topicJava, "What is Java?", DifficultyRank.EASY));
        Response response1 = new Response("Java is a programming language", true);
        Response response2 = new Response("Java is a technology", true);
        Response response3 = new Response("It is a car", false);
        Response response4 = new Response("It is a book", false);

        daoQuestion.addResponse(question1, response1);
        daoQuestion.addResponse(question1, response2);
        daoQuestion.addResponse(question1, response3);
        daoQuestion.addResponse(question1, response4);

        Question questionAfter = daoQuestion.findById(question1.getId()).orElseThrow(
                () -> new NotFoundException("Question not found")
        );

        List<Response> responses = questionAfter.getResponseList();
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(List.of(response1, response2, response3, response4));
    }

    @Test
    @DisplayName("It should remove a response from a question")
    public void itShouldRemoveAResponseFromAQuestion() {
        Topic topicJava = daoTopic.save(new Topic("Java"));
        Question question1 = daoQuestion.save(new Question(topicJava, "What is Java?", DifficultyRank.EASY));
        Response response1 = new Response("Java is a programming language", true);
        Response response2 = new Response("Java is a technology", true);
        Response response3 = new Response("It is a car", false);
        Response response4 = new Response("It is a book", false);

        daoQuestion.addResponse(question1, response1);
        daoQuestion.addResponse(question1, response2);
        daoQuestion.addResponse(question1, response3);
        daoQuestion.addResponse(question1, response4);

        daoQuestion.removeResponse(question1, response3);
        Question questionAfter = daoQuestion.findById(question1.getId()).orElseThrow(
                () -> new NotFoundException("Question not found")
        );

        List<Response> responses = questionAfter.getResponseList();
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(List.of(response1, response2, response4));
    }

    @Test
    @DisplayName("It should update a question")
    public void itShouldUpdateAQuestion() {
        Topic topicJava = daoTopic.save(new Topic("Java"));
        Topic topicSchool = daoTopic.save(new Topic("School"));
        Question initial = daoQuestion.save(new Question(topicJava, "What is Java?", DifficultyRank.EASY));

        Question expected = new Question(initial.getId(), topicSchool, "What is Python?", DifficultyRank.HARD);

        int rowsAffected = daoQuestion.update(expected);

        Question actual = daoQuestion.findById(initial.getId()).orElseThrow(
                () -> new NotFoundException("Question not found")
        );

        assertThat(rowsAffected).isEqualTo(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("It should delete a question")
    public void itShouldDeleteAQuestion() {
        Topic topicJava = daoTopic.save(new Topic("Java"));
        Question question = daoQuestion.save(new Question(topicJava, "What is Java?", DifficultyRank.EASY));

        int rowsAffected = daoQuestion.delete(question);

        assertThat(rowsAffected).isEqualTo(1);
        assertThat(daoQuestion.findById(question.getId())).isEmpty();
    }

    @Test
    @DisplayName("It should find a question by id")
    public void itShouldFindQuestionById() {
        Topic topicJava = daoTopic.save(new Topic( "Java"));
        Question expected = daoQuestion.save(new Question(topicJava, "What is Java?", DifficultyRank.EASY));

        Question actual = daoQuestion.findById(expected.getId()).orElseThrow(
                () -> new NotFoundException("Question not found")
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("It should save a question")
    public void itShouldSaveQuestion() {
        Topic topicJava = daoTopic.save(new Topic("Java"));
        Question expected = new Question(topicJava, "What is Java?", DifficultyRank.EASY);

        Question actual = daoQuestion.save(expected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("It shouldn't save a question with a non-existent topic")
    public void itShouldNotSaveQuestionWithNonExistentTopic() {
        Question question = new Question(new Topic(1, "Non-existent"), "What is Java?", DifficultyRank.EASY);

        assertThatThrownBy(() -> daoQuestion.save(question));
    }

    @Test
    @DisplayName("It should find all questions")
    public void itShouldFindAllQuestions() {
        Topic topicJava = daoTopic.save(new Topic("Java"));
        Question question1 = daoQuestion.save(new Question(topicJava, "What is Java?", DifficultyRank.EASY));
        Question question2 = daoQuestion.save(new Question(topicJava, "What is an interface?", DifficultyRank.MEDIUM));
        Question question3 = daoQuestion.save(new Question(topicJava, "What is an abstract class?", DifficultyRank.HARD));

        List<Question> questions = daoQuestion.findAll();

        assertThat(questions).usingRecursiveComparison()
                .ignoringFields("responseList")
                .isEqualTo(List.of(question1, question2, question3));
    }
}
