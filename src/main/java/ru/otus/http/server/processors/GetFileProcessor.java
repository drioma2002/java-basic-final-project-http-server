package ru.otus.http.server.processors;

import ru.otus.http.server.HttpRequest;
import ru.otus.http.server.response.ResponseContentType;
import ru.otus.http.server.response.ResponseStatusCode;
import ru.otus.http.server.response.ResponseTemplate;

import java.io.IOException;
import java.io.OutputStream;

import static ru.otus.http.server.app.FileUtils.readFile;

public class GetFileProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        String fileName = request.getUri().substring(1);

        ResponseTemplate responseTemplate = new ResponseTemplate();

        byte[] response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_200_OK, ResponseContentType.BINARY, readFile(fileName));
        output.write(response);
    }
}
