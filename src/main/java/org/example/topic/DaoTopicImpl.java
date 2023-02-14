package org.example.topic;

import org.example.database.ConnectionFactory;
import org.example.database.DbDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoTopicImpl extends DbDao<Topic> implements DaoTopic {
    @Override
    public Topic save(Topic topic) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "INSERT INTO topic(topic_name) VALUES (?);";
        try {
            preparedStatement = connection.prepareStatement(queryString, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, topic.getName());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                topic.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return topic;
    }

    @Override
    public Integer delete(Topic topic) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "DELETE FROM topic WHERE id = ?;";
        int rowsAffected;
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, topic.getId());
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
    public Integer update(Topic topic) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "UPDATE topic SET topic_name = ? WHERE id = ?;";
        int rowsAffected;
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, topic.getName());
            preparedStatement.setInt(2, topic.getId());
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
    public List<Topic> findAll() {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "SELECT * FROM topic;";
        List<Topic> topics = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Topic topic = new Topic(
                        resultSet.getInt("id"),
                        resultSet.getString("topic_name")
                );
                topics.add(topic);
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return topics;
    }

    public Optional<Topic> findById(Integer id) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "SELECT * FROM topic WHERE id = ?;";
        Topic topic = null;
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                topic = new Topic(
                        resultSet.getInt("id"),
                        resultSet.getString("topic_name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return Optional.ofNullable(topic);
    }

    @Override
    public Optional<Topic> findByName(String name) {
        connection = ConnectionFactory.getInstance().getConnection();
        String queryString = "SELECT * FROM topic WHERE topic_name = ?;";
        Topic topic = null;
        try {
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                topic = new Topic(
                        resultSet.getInt("id"),
                        resultSet.getString("topic_name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error when executing [%s]", queryString), e);
        }
        finally {
            cleanUp();
        }
        return Optional.ofNullable(topic);
    }
}
