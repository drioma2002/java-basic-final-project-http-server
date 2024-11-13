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

public class CreateNewItemsProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;

    public CreateNewItemsProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException, SQLException {
        Gson gson = new Gson();
        Item item = itemsRepository.save(gson.fromJson(request.getBody(), Item.class));

        ResponseTemplate responseTemplate = new ResponseTemplate();
        String response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_201_Created, ResponseContentType.JSON, gson.toJson(item));

        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
