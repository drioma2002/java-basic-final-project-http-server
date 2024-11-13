package ru.otus.http.server.response;

public enum ResponseContentType {
    HTML ("text/html"),
    JSON ("application/json"),
    BINARY ("application/octet-stream");

    public final String TYPE;

    ResponseContentType(String TYPE) {
        this.TYPE = TYPE;
    }
}
