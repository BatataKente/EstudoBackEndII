package org.example.utils;

public enum Methods {
    GET, POST, PUT, DELETE;
    public static Methods get(String method) {
        switch(method) {
            case "GET" -> {
                return GET;
            }
            case "PUT" -> {
                return PUT;
            }
            case "POST" -> {
                return POST;
            }
            case "DELETE" -> {
                return DELETE;
            }
            default -> {
                return null;
            }
        }
    }
}
