package com.example.patterns_banking.repositories;

import com.example.patterns_banking.models.Customer;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerRepository extends BaseRepository<Customer> {

    @Getter
    private static final CustomerRepository instance = new CustomerRepository();

    private CustomerRepository() {
        super();
    }

    @Override
    protected String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS customers (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "email VARCHAR(50) NOT NULL UNIQUE)";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO customers(name, email) VALUES (?, ?)";
    }

    @Override
    protected void mapToStatement(PreparedStatement stmt, Customer customer) throws SQLException {
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getEmail());
    }

    @Override
    protected Customer setGeneratedId(Customer customer, Long id) {
        customer.setId(id);
        return customer;
    }
}

