package org.example.database;

import org.example.dao.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class DbDao<T> implements Dao<T> {
    protected Connection connection;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    public abstract T save(T t);
    public abstract Integer delete(T t);
    public abstract Integer update(T t);
    public abstract List<T> findAll();
    public abstract Optional<T> findById(Integer id);

    protected void cleanUp() {
        try {
            if (connection != null)
                connection.close();
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (resultSet != null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
