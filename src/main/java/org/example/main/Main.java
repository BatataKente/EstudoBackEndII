package org.example.main;

import org.example.controllers.ProjectController;
import org.example.models.Project;
import org.example.utils.SqlDatabase;

import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws SQLException {
        SqlDatabase.connect();

        final var controller = new ProjectController();


        final var projects = controller.selectProjects();

        projects.forEach(System.out::println);
    }
}