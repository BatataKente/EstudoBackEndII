package org.example.repositories;

import com.google.gson.GsonBuilder;
import org.example.controllers.ProjectController;
import org.example.models.EndPoint;
import org.example.models.Project;
import org.example.utils.Methods;

import java.sql.SQLException;
import java.util.function.Function;

public class ProjectRepository extends EndPoint {
    private final static String PATH = "/projects";
    private final Function response = header -> {
        try {
            switch(Methods.get(getExchange().getRequestMethod())) {
                case GET -> {
                    return ProjectController.getAll();
                }
                default -> {
                    return "Error unknown method";
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    };
    public ProjectRepository() {
        super(PATH);
        setResponse(response);
    }
}
