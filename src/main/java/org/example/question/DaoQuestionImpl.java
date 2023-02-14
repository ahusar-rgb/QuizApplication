package org.example.question;

import lombok.RequiredArgsConstructor;
import org.example.dao.DaoFactory;
import org.example.database.ConnectionFactory;
import org.example.database.DbDao;
import org.example.response.Response;
import org.example.topic.Topic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DaoQuestionImpl extends DbDao<Question> implements DaoQuestion {
    public Question save(Question question) {
        String queryString =
                "INSERT INTO " +
                "question(topic_id, question_content, difficulty_rank) " +
                "VALUES (?, ?, ?);";

        connection = ConnectionFactory.getInstance().getConnection();

        try {
            preparedStatement = connection.prepareStatement(queryString, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, question.getTopic().getId());
            preparedStatement.setString(2, question.getContent());
            preparedStatement.setInt(3, question.getDifficultyRank().ordinal());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                question.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return question;
    }

    public Integer delete(Question question) {
        String queryString = "DELETE FROM question WHERE id = ?;";
        int rowsAffected;

        connection = ConnectionFactory.getInstance().getConnection();

        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, question.getId());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }

        return rowsAffected;
    }

    public Integer update(Question question) {
        String queryString =
                "UPDATE question " +
                "SET topic_id = ?, question_content = ?, difficulty_rank = ? " +
                "WHERE id = ?;";

        int rowsAffected;

        connection = ConnectionFactory.getInstance().getConnection();

        try {
            preparedStatement = connection.prepareStatement(queryString);
            try {
                preparedStatement.setInt(1, question.getTopic().getId());
            } catch (NullPointerException e) {
                throw new RuntimeException("Topic or its id cannot be null");
            }
            preparedStatement.setString(2, question.getContent());
            preparedStatement.setInt(3, question.getDifficultyRank().ordinal());
            preparedStatement.setInt(4, question.getId());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }

        return rowsAffected;
    }

    public List<Question> findAll() {
        String queryString = "SELECT * FROM question;";
        List<Question> result = new ArrayList<>();

        connection = ConnectionFactory.getInstance().getConnection();

        try {
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                result.add(rowToQuestion(resultSet));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(String.format("Error occurred when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return result;
    }

    public Optional<Question> findById(Integer id) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "SELECT * FROM question WHERE id = ?;";
        Question question = null;
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                question = rowToQuestion(resultSet);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(String.format("Error occurred when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return Optional.ofNullable(question);
    }

    private Question rowToQuestion(ResultSet resultSet) {
        Question question;
        try {
             question = new Question(
                    resultSet.getInt("id"),
                    DaoFactory.getInstance().getDaoTopic().findById(resultSet.getInt("topic_id")).orElseThrow(
                            () -> new RuntimeException("Topic not found")
                    ),
                    resultSet.getString("question_content"),
                    DifficultyRank.values()[resultSet.getInt("difficulty_rank")]
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Response> responseList = DaoFactory.getInstance().getDaoResponse().findByQuestion(question);
        responseList.forEach(question::addResponse);
        return question;
    }

    @Override
    public List<Question> findByTopic(Topic topic) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "SELECT * FROM question WHERE topic_id = ?;";
        List<Question> result = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(
                    1,
                    DaoFactory.getInstance().getDaoTopic()
                            .findByName(topic.getName()).orElseThrow(
                                    () -> new RuntimeException("Topic not found")
                            ).getId()
            );
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(rowToQuestion(resultSet));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(String.format("Error occurred when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return result;
    }

    @Override
    public void addResponse(Question question, Response response) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "INSERT INTO question_response(question_id, response_id) VALUES (?, ?);";

        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, question.getId());
            Response savedResponse = DaoFactory.getInstance().getDaoResponse().save(response);
            preparedStatement.setInt(2, savedResponse.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            cleanUp();
        }
        question.addResponse(response);
    }

    @Override
    public void removeResponse(Question question, Response response) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "DELETE FROM question_response WHERE question_id = ? AND response_id = ?;";

        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, question.getId());
            preparedStatement.setInt(2, response.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            cleanUp();
        }
        question.removeResponse(response);
    }
}
