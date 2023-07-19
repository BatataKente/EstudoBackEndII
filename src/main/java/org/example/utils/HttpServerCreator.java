package org.example.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import org.example.models.EndPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class HttpServerCreator {
    EndPoint[] endPoints;
    public HttpServerCreator(EndPoint[] endPoints) {
        this.endPoints = endPoints;
    }
    public void open() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8000), 0);
            Arrays.stream(endPoints).forEach(
                endPoint -> {
                    server.createContext(
                        endPoint.getPath(),
                        exchange -> {
                            Headers headers = exchange.getResponseHeaders();
                            headers.add("Access-Control-Allow-Origin", "http://localhost:3000");
                            headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
                            headers.add("Access-Control-Allow-Methods", exchange.getRequestMethod());
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
                            }
                            endPoint.setExchange(exchange);
                            String response = endPoint.getResponse(requestBody);
                            byte[] responseBytes = response.getBytes();
                            exchange.sendResponseHeaders(200, responseBytes.length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(responseBytes);
                            outputStream.close();
                        }
                    );
                }
            );
            server.setExecutor(null);
            server.start();
            System.out.println("Servidor iniciado na porta 8000.");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
