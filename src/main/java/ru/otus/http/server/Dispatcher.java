package ru.otus.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.http.server.app.ItemsRepository;
import ru.otus.http.server.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static ru.otus.http.server.app.FileUtils.fileExists;

public class Dispatcher {
    private Map<String, RequestProcessor> processors;
    private RequestProcessor defaultNotFoundProcessor;
    private RequestProcessor defaultInternalServerErrorProcessor;
    private RequestProcessor defaultBadRequestProcessor;
    private RequestProcessor defaultMethodNotAllowedProcessor;
    private RequestProcessor getFileProcessor;
    private Set<String> allAvailUries;
    private static final Logger LOGGER = LogManager.getLogger(Dispatcher.class.getName());

    public Dispatcher(ItemsRepository itemsRepository) {
        this.processors = new HashMap<>();
        this.processors.put("GET /items", new GetAllItemsProcessor(itemsRepository));
        this.processors.put("GET /items?", new GetItemByParamProcessor(itemsRepository));
        this.processors.put("GET /items/", new GetItemByPatchProcessor(itemsRepository));
        this.processors.put("POST /items", new CreateNewItemsProcessor(itemsRepository));
        this.processors.put("PUT /items", new UpdateItemProcessor(itemsRepository));
        this.processors.put("DELETE /items?", new DeleteItemByParamProcessor(itemsRepository));
        this.processors.put("DELETE /items/", new DeleteItemByPatchProcessor(itemsRepository));
        this.defaultNotFoundProcessor = new DefaultNotFoundProcessor();
        this.defaultInternalServerErrorProcessor = new DefaultInternalServerErrorProcessor();
        this.defaultBadRequestProcessor = new DefaultBadRequestProcessor();
        this.defaultMethodNotAllowedProcessor = new DefaultMethodNotAllowedProcessor();
        this.getFileProcessor = new GetFileProcessor();

        allAvailUries = new HashSet<>();
        for (String routingKey : processors.keySet()) {
            String[] uri = routingKey.split(" ", 2);
            allAvailUries.add(uri[1]);
        }

        processors.forEach((key, value) -> LOGGER.debug("Available rout: '" + key + "'"));
        allAvailUries.forEach((key) -> LOGGER.debug("Available URI: '" + key + "'"));
    }

    public void execute(HttpRequest request, OutputStream out) throws IOException, SQLException {
        LOGGER.debug("RouteKey: " + request.getRoutingKey());
        try {
            if (!processors.containsKey(request.getRoutingKey())) {
                String fileName = request.getUri().substring(1);
                if (fileExists(fileName)) {
                    getFileProcessor.execute(request, out);
                    return;
                }

                if (allAvailUries.contains(request.getUri())) {
                    defaultMethodNotAllowedProcessor.execute(request, out);
                    return;
                }

                defaultNotFoundProcessor.execute(request, out);
                return;
            }
            processors.get(request.getRoutingKey()).execute(request, out);
        } catch (BadRequestException e) {
            request.setException(e);
            defaultBadRequestProcessor.execute(request, out);
        } catch (Exception e) {
            e.printStackTrace();
            defaultInternalServerErrorProcessor.execute(request, out);
        }
    }
}
