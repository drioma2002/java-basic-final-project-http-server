package ru.otus.http.server.processors;

import ru.otus.http.server.HttpRequest;
import ru.otus.http.server.response.ResponseContentType;
import ru.otus.http.server.response.ResponseStatusCode;
import ru.otus.http.server.response.ResponseTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultNotFoundProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        ResponseTemplate responseTemplate = new ResponseTemplate();
        String body = "<html><body><h1>Page Not Found</h1></body></html>";
        String response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_404_NotFound, ResponseContentType.HTML, body);

        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
