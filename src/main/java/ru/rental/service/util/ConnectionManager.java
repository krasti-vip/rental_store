package ru.rental.service.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String BD_URL = "db.url";
    private static final String BD_USERNAME = "db.username";
    private static final String BD_PASSWORD = "db.password";

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getProperties(BD_URL),
                    PropertiesUtil.getProperties(BD_USERNAME),
                    PropertiesUtil.getProperties(BD_PASSWORD));
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка подключения к базе данных", e);
        }
    }
}
