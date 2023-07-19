package org.example.controllers;

import org.example.models.Project;
import org.example.utils.SqlDatabase;
import org.example.utils.SqlTable;

import java.sql.*;
import java.util.List;

public class ProjectController {
    private static SqlTable table = new SqlTable("projects");
    public static void post(Project project) throws SQLException {
        table.insert(
            new String [] {"name", "description", "created_at", "updated_at",},
            statement -> {
                try {
                    statement.setString(1, project.getName());
                    statement.setString(2, project.getDescription());
                    statement.setDate(3, new Date(project.getCreatedAt().getTime()));
                    statement.setDate(4, new Date(project.getUpdatedAt().getTime()));
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }
        );
    }
    public static List<Object> getAll() throws SQLException {
        return table.selectAll(
            result -> {
                var project = new Project();
                try {
                    project.setId(result.getInt("id"));
                    project.setName(result.getString("name"));
                    project.setDescription(result.getString("description"));
                    project.setCreatedAt(result.getTimestamp("created_at"));
                    project.setUpdatedAt(result.getTimestamp("updated_at"));
                    return project;
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }
        );
    }
    public static void update(Project project) throws SQLException {
        String[] parameters = {
                "name", "description", "createdAt", "updatedAt"
        };
        String script = "UPDATE projects SET " +
                table.joiningWithQuestionMarkAndComma.apply(parameters) + " WHERE id = ?";
        try(Connection connection = SqlDatabase.connect();
            PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setString(1, project.getName());
            statement.setString(2, project.getDescription());
            statement.setDate(3, new Date(project.getCreatedAt().getTime()));
            statement.setDate(4, new Date(project.getUpdatedAt().getTime()));
            statement.setInt(5, project.getId());
            statement.execute();
        } catch(SQLException error) {
            System.out.print("Erro ao fazer operação update no projeto: " + error);
            throw(error);
        }
    }
    public static void deleteById(int id) throws SQLException {
        table.deleteById(id);
    }
    public static void delete(Project project) throws SQLException {
        table.delete(
            "id",
            statement -> {
                try {
                    statement.setInt(1, project.getId());
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }
        );
    }
}
