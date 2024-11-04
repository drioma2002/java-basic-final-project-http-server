package ru.otus.http.server.processors;

import ru.otus.http.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public interface RequestProcessor {
    void execute(HttpRequest request, OutputStream output) throws IOException, SQLException;
}
