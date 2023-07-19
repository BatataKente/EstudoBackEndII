package org.example.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.example.controllers.ProjectController;
import org.example.models.EndPoint;
import org.example.models.Project;
import org.example.repositories.ProjectRepository;
import org.example.utils.HttpServerCreator;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        var server = new HttpServerCreator(
            new EndPoint[] {
                new EndPoint("/", header -> "Olá, mundo!"),
                new EndPoint("/lero", header -> "lero lero"),
                new ProjectRepository()
            }
        );
        server.open();
    }
}