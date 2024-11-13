package ru.otus.http.server.processors;

import com.google.gson.Gson;
import ru.otus.http.server.HttpRequest;
import ru.otus.http.server.app.Item;
import ru.otus.http.server.app.ItemsRepository;
import ru.otus.http.server.response.ResponseContentType;
import ru.otus.http.server.response.ResponseStatusCode;
import ru.otus.http.server.response.ResponseTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class UpdateItemProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;

    public UpdateItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException, SQLException {
        Gson gson = new Gson();
        Item item = gson.fromJson(request.getBody(), Item.class);
        Item updatedItem = itemsRepository.updateItem(item);

        String response = "";
        ResponseTemplate responseTemplate = new ResponseTemplate();

        if (updatedItem != null) {
            String itemsJson = gson.toJson(updatedItem);
            response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_200_OK, ResponseContentType.JSON, itemsJson);

        } else {
            String body = "{\"id\":" + item.getId() + "}";
            response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_422_UnprocessableEntity, ResponseContentType.JSON, body);
        }

        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
