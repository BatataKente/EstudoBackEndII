package org.example.models;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.example.controllers.ProjectController;
import org.example.utils.Methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class EndPoint {
    private final String path;
    private Function response;
    private HttpExchange exchange;
    public EndPoint(String path) {
        this.path = path;
    }
    public EndPoint(String path, Function response) {
        this.path = path;
        this.response = response;
    }
    public EndPoint(String path, Function get, Function put, Function post, Function delete) {
        this.path = path;
        setResponse(get, put, post, delete);
    }
    public void setExchange(HttpExchange exchange) {
        this.exchange = exchange;
    }
    public void setResponse(Function response) {
        this.response = response;
    }
    protected HttpExchange getExchange() {
        return exchange;
    }
    public String getPath() {
        return path;
    }
    public String getResponse(StringBuilder header) {
        return new Gson().toJson(response.apply(header));
    }
    private Object setResponse(Function get, Function put, Function post, Function delete) {
        StringBuilder requestBody = new StringBuilder();
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody())
            )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        switch(Methods.get(getExchange().getRequestMethod())) {
            case GET -> {
                return get.apply(requestBody);
            }
            case PUT -> {
                return put.apply(requestBody);
            }
            case POST -> {
                return post.apply(requestBody);
            }
            case DELETE -> {
                return delete.apply(requestBody);
            }
            default -> {
                return "Error unknown method";
            }
        }
    }
}
