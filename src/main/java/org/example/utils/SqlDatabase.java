package org.example.utils;

import java.sql.*;

public abstract class SqlDatabase {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/todoapp";
    private static final String USER = "root";
    private static final String PASS = "";
    private enum Errors {
        OPENING,
        CLOSING
    }
    private static String getErrorMessage(Errors error) {
        switch(error) {
            case OPENING -> {
                return "Erro ao efetuar conexão com banco de dados.";
            }
            case CLOSING -> {
                return "Erro ao fechar conexão com banco de dados.";
            }
            default -> {
                return "unknown error";
            }
        }
    }
    public static Connection connect() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch(ClassNotFoundException | SQLException error) {
            throw new RuntimeException(getErrorMessage(Errors.OPENING), error);
        }
    }
    public static void close(Connection connection) {
        try {
            if(connection != null) connection.close();
        } catch(SQLException error) {
            throw new RuntimeException(getErrorMessage(Errors.CLOSING), error);
        }
    }
    public static void close(PreparedStatement statement) {
        try {
            if(statement != null) statement.close();
        } catch(SQLException error) {
            throw new RuntimeException(getErrorMessage(Errors.CLOSING), error);
        }
    }
    public static void close(ResultSet result) {
        try {
            if(result != null) result.close();
        } catch(SQLException error) {
            throw new RuntimeException(getErrorMessage(Errors.CLOSING), error);
        }
    }
}
