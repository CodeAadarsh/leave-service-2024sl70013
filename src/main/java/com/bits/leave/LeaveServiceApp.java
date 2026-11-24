package com.bits.leave;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class LeaveServiceApp {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        String env = System.getenv("APP_ENV");
        if (env == null) env = "development";

        LeaveCalculator calculator = new LeaveCalculator();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", exchange ->
            sendResponse(exchange, 200,
                "Employee Leave Service - Environment: " + env + "\n"));

        server.createContext("/health", exchange ->
            sendResponse(exchange, 200,
                "{\"status\":\"UP\",\"env\":\"" + env + "\"}\n"));

        server.createContext("/leave/remaining", exchange -> {
            int remaining = calculator.calculateRemainingLeave(30, 10);
            sendResponse(exchange, 200,
                "{\"remaining\":" + remaining + ",\"env\":\"" + env + "\"}\n");
        });

        server.start();
        System.out.println("Leave Service started on port " + port + " [env=" + env + "]");
    }

    private static void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
        byte[] bytes = body.getBytes();
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
