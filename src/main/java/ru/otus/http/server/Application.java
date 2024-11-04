package ru.otus.http.server;

import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException {
        int serverPort = Integer.parseInt((String) System.getProperties().getOrDefault("port", "8189"));
        String dbURL = (String) System.getProperties().getOrDefault("url", "jdbc:postgresql://192.168.1.91:5433/products_db");
        String dbUser = (String) System.getProperties().getOrDefault("user", "postgres");
        String dbPassword = (String) System.getProperties().getOrDefault("password", "password");

        new HttpServer(serverPort, dbURL, dbUser, dbPassword).start();
    }
}
