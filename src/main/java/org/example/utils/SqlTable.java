package org.example.utils;

import org.example.utils.SqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SqlTable {
    private String name;
    public SqlTable(String name) {
        this.name = name;
    }
    private static void getException(String script, SQLException exception) throws SQLException {
        System.out.print("Erro ao executar " + script + "\n" + exception);
        throw(exception);
    }
    private static final Function<String[], String> joiningWithComma = array -> String.join(", ", array);
    private static final Function<String[], String> joiningAsQuestionMark = array -> {
        return String.join(", ", Arrays.stream(array)
                .map(parameter -> "?")
                .toArray(String[]::new));
    };
    public static final Function<String[], String> joiningWithQuestionMarkAndComma = array -> {
        return String.join(", ", Arrays.stream(array)
                .map(parameter -> parameter + " = ?")
                .toArray(String[]::new));
    };
    public List<Object> selectAll(Function<ResultSet, Object> columns) throws SQLException {
        String script = "SELECT * FROM " + name;
        var lines = new ArrayList<Object>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = SqlDatabase.connect();
            statement = connection.prepareStatement(script);
            result = statement.executeQuery();
            while(result.next()) {
                lines.add(columns.apply(result));
            }
        } catch(SQLException exception) {
            getException(script, exception);
        } finally {
            SqlDatabase.close(connection);
            SqlDatabase.close(statement);
            SqlDatabase.close(result);
        }
        return lines;
    }
    public void insert(
            String[] columns, Consumer<PreparedStatement> lines
    ) throws SQLException {
        var script = "INSERT INTO " + name + " (" + joiningWithComma.apply(columns) + ") " +
                " VALUES (" + joiningAsQuestionMark.apply(columns) + ")";
        try(Connection connection = SqlDatabase.connect();
            PreparedStatement statement = connection.prepareStatement(script)) {
            lines.accept(statement);
            statement.execute();
        } catch(SQLException exception) {
            getException(script, exception);
        }
    }
    public void delete(
            String column, Consumer<PreparedStatement> lines
    ) throws SQLException {
        var script = "DELETE FROM " + name + " WHERE " + column + " = ?";
        try (Connection connection = SqlDatabase.connect();
             PreparedStatement statement = connection.prepareStatement(script)) {
            lines.accept(statement);
            statement.execute();
        } catch(SQLException exception) {
            getException(script, exception);
        }
    }
    public void deleteById(int id) throws SQLException {
        var script = "DELETE FROM " + name + " WHERE id = ?";
        try (Connection connection = SqlDatabase.connect();
             PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, id);
            statement.execute();
        } catch(SQLException exception) {
            getException(script, exception);
        }
    }
}
