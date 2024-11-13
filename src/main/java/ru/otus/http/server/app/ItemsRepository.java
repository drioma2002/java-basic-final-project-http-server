package ru.otus.http.server.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsRepository {

    private final String USER;
    private final String PASSWORD;
    private final String DATABASE_URL;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private static final Logger LOGGER = LogManager.getLogger(ItemsRepository.class.getName());

    public ItemsRepository(String url, String user, String password) throws SQLException {
        this.USER = user;
        this.PASSWORD = password;
        this.DATABASE_URL = url;
        LOGGER.info("Подключение к БД под пользователем " + user);
        LOGGER.info("Url БД: " + url);
        this.connection = getConnection();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }

    public List<Item> getItems() throws SQLException {
        LOGGER.debug("Получаем список всех item-ов");

        List<Item> list = new ArrayList<>();
        preparedStatement = connection.prepareStatement("SELECT id, title, price FROM product");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            list.add(
                    new Item(
                            resultSet.getLong("id"),
                            resultSet.getString("title"),
                            resultSet.getBigDecimal("price")
                    )
            );
        }
        return list;
    }

    public Item save(Item item) throws SQLException {
        String title = item.getTitle();
        BigDecimal price = item.getPrice();
        preparedStatement = connection.prepareStatement("INSERT INTO product (title, price) VALUES (?, ?)", new String[] {"id"});
        preparedStatement.setString(1, title);
        preparedStatement.setBigDecimal(2, price);
        preparedStatement.executeUpdate();

        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            item.setId(rs.getLong(1));
        }

        LOGGER.debug("Сохраняем item: " + item);

        return item;
    }

    public Item getItem(Long id) throws SQLException {
        Item item = new Item();
        LOGGER.debug("Получаем item id=" + id);
        preparedStatement = connection.prepareStatement("SELECT id, title, price FROM product WHERE id = ?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String title = resultSet.getString("title");
            BigDecimal price = resultSet.getBigDecimal("price");
            item.setId(id);
            item.setTitle(title);
            item.setPrice(price);
        }
        return item;
    }

    public int deleteItem(Long id) throws SQLException {
        Item item = new Item();
        LOGGER.debug("Удаляем item id=" + id);
        preparedStatement = connection.prepareStatement("DELETE FROM product WHERE id = ?");
        preparedStatement.setLong(1, id);

        return preparedStatement.executeUpdate();
    }

    public Item updateItem(Item item) throws SQLException {
        LOGGER.debug("Обновляем item id=" + item.getId());
        preparedStatement = connection.prepareStatement("UPDATE product SET title = ?, price = ? WHERE id = ?");
        preparedStatement.setString(1, item.getTitle());
        preparedStatement.setBigDecimal(2, item.getPrice());
        preparedStatement.setLong(3, item.getId());

        preparedStatement.executeUpdate();

        if (preparedStatement.executeUpdate() > 0) {
            return getItem(item.getId());
        } else {
            return null;
        }
    }

}
