package com.example.patterns_banking.repositories;

import com.example.patterns_banking.models.Account;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountRepository extends BaseRepository<Account> {

    @Getter
    private static final AccountRepository instance = new AccountRepository();

    private AccountRepository() {
        super();
    }

    @Override
    protected String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS account (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "number VARCHAR(255) NOT NULL UNIQUE, " +
                "type VARCHAR(50) NOT NULL, " +
                "balance NUMERIC(19, 2), " +
                "is_active BOOLEAN DEFAULT TRUE)";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO account(number, type, balance, is_active) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void mapToStatement(PreparedStatement stmt, Account acc) throws SQLException {
        stmt.setString(1, acc.getNumber());
        stmt.setString(2, acc.getType().toString());
        stmt.setBigDecimal(3, acc.getBalance());
        stmt.setBoolean(4, Boolean.TRUE.equals(acc.getIsActive()));
    }

    @Override
    protected Account setGeneratedId(Account acc, Long id) {
        acc.setId(id);
        return acc;
    }
}

