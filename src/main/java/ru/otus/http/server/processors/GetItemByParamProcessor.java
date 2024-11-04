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

public class GetItemByParamProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;

    public GetItemByParamProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException, SQLException {
        Long id = Long.parseLong(request.getParameter("id"));
        Item item = itemsRepository.getItem(id);

        String response = "";
        ResponseTemplate responseTemplate = new ResponseTemplate();
        if (item.getId() == null) {
            response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_204_NoContent);
        } else {
            Gson gson = new Gson();
            String itemsJson = gson.toJson(item);
            response = responseTemplate.prepareResponse(ResponseStatusCode.CODE_200_OK, ResponseContentType.JSON, itemsJson);
        }
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
