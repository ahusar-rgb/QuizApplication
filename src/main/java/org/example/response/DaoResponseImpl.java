package org.example.response;

import org.example.database.ConnectionFactory;
import org.example.database.DbDao;
import org.example.question.Question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoResponseImpl extends DbDao<Response> implements DaoResponse {

    @Override
    public Response save(Response response) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "INSERT INTO response(response_text, is_correct) VALUES (?, ?);";
        try {
            preparedStatement = connection.prepareStatement(queryString, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, response.getText());
            preparedStatement.setBoolean(2, response.isCorrect());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                response.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return response;
    }

    @Override
    public Integer delete(Response response) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "DELETE FROM response WHERE id = ?;";
        int rowsAffected;
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, response.getId());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return rowsAffected;
    }

    @Override
    public Integer update(Response response) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "UPDATE response SET response_text = ?, is_correct = ? WHERE id = ?;";
        int rowsAffected;
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, response.getText());
            preparedStatement.setBoolean(2, response.isCorrect());
            preparedStatement.setInt(3, response.getId());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return rowsAffected;
    }

    @Override
    public List<Response> findAll() {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "SELECT * FROM response;";
        List<Response> responses = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Response response = new Response(
                        resultSet.getInt("id"),
                        resultSet.getString("response_text"),
                        resultSet.getBoolean("is_correct")
                );
                responses.add(response);
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return responses;
    }

    @Override
    public Optional<Response> findById(Integer id) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "SELECT * FROM response WHERE id = ?;";
        Response response = null;
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                response = new Response(
                        resultSet.getInt("id"),
                        resultSet.getString("response_text"),
                        resultSet.getBoolean("is_correct")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }

        finally {
            cleanUp();
        }
        return Optional.ofNullable(response);
    }

    public List<Response> findByQuestion(Question question) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString =
                "SELECT r.id, r.response_text, r.is_correct " +
                "FROM response r " +
                "JOIN question_response qr ON qr.response_id = r.id " +
                "WHERE qr.question_id = ?;";
        List<Response> responses = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, question.getId());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Response response = new Response(
                        resultSet.getInt("id"),
                        resultSet.getString("response_text"),
                        resultSet.getBoolean("is_correct")
                );
                responses.add(response);
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return responses;
    }
}
