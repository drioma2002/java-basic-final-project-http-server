package ru.otus.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.http.server.app.ItemsRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int serverPort;
    private Dispatcher dispatcher;
    private static final Logger LOGGER = LogManager.getLogger(HttpServer.class.getName());

    public HttpServer(int serverPort, String dbURL, String dbUser, String dbPassword) throws SQLException {
        this.serverPort = serverPort;
        this.dispatcher = new Dispatcher(new ItemsRepository(dbURL, dbUser, dbPassword));
    }

    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            LOGGER.info("Сервер запущен на порту: " + serverPort);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new RequestHandler(socket, dispatcher));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
