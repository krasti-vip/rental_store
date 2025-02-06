package ru.rental.servic.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class RentalConfig {

    private final static Properties menu = new Properties();

    static {
        try (InputStreamReader reader = new InputStreamReader(
                RentalConfig.class.getClassLoader().getResourceAsStream("sout.properties"), StandardCharsets.UTF_8)) {
            menu.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RentalConfig() {
    }

    public static String getPropertyMenu(String key) {
        return menu.getProperty(key);
    }
}
