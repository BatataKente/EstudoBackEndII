package org.example.controllers;

import org.example.models.Project;
import org.example.utils.SqlDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectController extends Controller {
    public void insert(Project project) throws SQLException {
        String[] parameters = {
                "name", "description", "created_at", "updated_at"
        };
        var script = "INSERT INTO projects (" + joiningWithComma.apply(parameters) + ") " +
                " VALUES (" + joiningAsQuestionMark.apply(parameters) + ")";
        try(Connection connection = SqlDatabase.connect();
            PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setString(1, project.getName());
            statement.setString(2, project.getDescription());
            statement.setDate(3, new java.sql.Date(project.getCreatedAt().getTime()));
            statement.setDate(4, new java.sql.Date(project.getUpdatedAt().getTime()));
            statement.execute();
        } catch(SQLException error) {
            System.out.print("Erro ao fazer operação insert no projeto: " + error);
            throw(error);
        }
    }
    public List<Project> selectProjects() throws SQLException {
        String script = "SELECT * FROM projects";
        var projects = new ArrayList<Project>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = SqlDatabase.connect();
            statement = connection.prepareStatement(script);
            result = statement.executeQuery();
            while(result.next()) {
                var project = new Project();
                project.setId(result.getInt("id"));
                project.setName(result.getString("name"));
                project.setDescription(result.getString("description"));
                project.setCreatedAt(result.getDate("created_at"));
                project.setUpdatedAt(result.getDate("updated_at"));
                projects.add(project);
            }
        } catch(SQLException error) {
            System.out.print("Erro ao selecionar projeto: " + error);
            throw(error);
        } finally {
            SqlDatabase.close(connection);
            SqlDatabase.close(statement);
            SqlDatabase.close(result);
        }
        return projects;
    }
}
