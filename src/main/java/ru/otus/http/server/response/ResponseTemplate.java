package ru.otus.http.server.response;

import java.nio.charset.StandardCharsets;

public class ResponseTemplate {

    public String prepareResponse(ResponseStatusCode responseStatusCode) {
        String responseTemplate = "" +
                "HTTP/1.1 " + responseStatusCode.STATUS_CODE + " " + responseStatusCode.DESCRIPTION + "\r\n" +
                "\r\n";

        return responseTemplate;
    }

    public String prepareResponse(ResponseStatusCode responseStatusCode, ResponseContentType responseContentType, String body) {
        String responseTemplate = "" +
                "HTTP/1.1 " + responseStatusCode.STATUS_CODE + " " + responseStatusCode.DESCRIPTION + "\r\n" +
                "Content-Type: " + responseContentType.TYPE + "\r\n" +
                "\r\n" +
                body;

        return responseTemplate;
    }

    public byte[] prepareResponse(ResponseStatusCode responseStatusCode, ResponseContentType responseContentType, byte[] bodyBytes) {
        String responseHeader = "" +
                "HTTP/1.1 " + responseStatusCode.STATUS_CODE + " " + responseStatusCode.DESCRIPTION + "\r\n" +
                "Content-Type: " + responseContentType.TYPE + "\r\n" +
                "\r\n";

        byte[] responseHeaderBytes = responseHeader.getBytes(StandardCharsets.UTF_8);
        byte[] responseTemplate = new byte[responseHeaderBytes.length + bodyBytes.length];

        System.arraycopy(responseHeaderBytes, 0, responseTemplate, 0, responseHeaderBytes.length);
        System.arraycopy(bodyBytes, 0, responseTemplate, responseHeaderBytes.length, bodyBytes.length);

        return responseTemplate;
    }
}
