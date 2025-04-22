package com.example.patterns_banking.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseRepository<T> {
    protected static final String DB_URL = "jdbc:h2:mem:patterns-banking";
    protected static final String DB_USER = "sa";
    protected static final String DB_PW = "";

    protected BaseRepository() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(getCreateTableSQL());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize table", e);
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
    }

    public T save(T entity) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getInsertSQL(), Statement.RETURN_GENERATED_KEYS)) {

            mapToStatement(pstmt, entity);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating entity failed, no rows affected.");
            }

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return setGeneratedId(entity, keys.getLong(1));
                } else {
                    throw new SQLException("Creating entity failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving entity", e);
        }
    }

    protected abstract String getCreateTableSQL();

    protected abstract String getInsertSQL();

    protected abstract void mapToStatement(PreparedStatement stmt, T entity) throws SQLException;

    protected abstract T setGeneratedId(T entity, Long id);
}

