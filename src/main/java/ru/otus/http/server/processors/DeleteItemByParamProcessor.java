package ru.otus.http.server.processors;

import ru.otus.http.server.HttpRequest;
import ru.otus.http.server.app.ItemsRepository;
import ru.otus.http.server.response.ResponseContentType;
import ru.otus.http.server.response.ResponseStatusCode;
import ru.otus.http.server.response.ResponseTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class DeleteItemByParamProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;

    public DeleteItemByParamProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException, SQLException {
        Long id = Long.parseLong(request.getParameter("id"));
        int deleted = itemsRepository.deleteItem(id);

        ResponseTemplate responseTemplate = new ResponseTemplate();
        String response = "";
        if (deleted > 0) {
            response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_204_NoContent);
        } else {
            String body = "{\"id\":" + id + "}";
            response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_422_UnprocessableEntity, ResponseContentType.JSON, body);
        }

        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
