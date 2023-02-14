package org.example.DbTest;

import org.example.database.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

public abstract class DbTest {
    @BeforeEach
    public void beforeEach() {
        clearDb();
    }

    private void clearDb() {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        try {
            connection.createStatement().execute("DELETE FROM question_response");
            connection.createStatement().execute("DELETE FROM response");
            connection.createStatement().execute("DELETE FROM question");
            connection.createStatement().execute("DELETE FROM topic");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
