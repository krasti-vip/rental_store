package ru.rental.service.util;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesUtil {

    private static final Logger log = (Logger) LoggerFactory.getLogger(PropertiesUtil.class.getName());
    private static final Properties PROPERTIES = new Properties();
    private static final Properties menu = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    public static String getProperties(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        log.info("Loading properties file: {}", "application.properties");
        try (final var resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(resourceAsStream);

            log.info("Load properties success");
        } catch (IOException e) {
            log.error("Load properties failed ", e);
            throw new IllegalStateException("Ошибка пропертиУтил загрузка данных", e);
        }
    }

    static {
        try (InputStreamReader reader = new InputStreamReader(
                PropertiesUtil.class.getClassLoader().getResourceAsStream("sout.properties"), StandardCharsets.UTF_8)) {
            menu.load(reader);
        } catch (IOException e) {
            log.error("Load menu failed ", e);
            throw new IllegalStateException("Ошибка пропертиУтил загрузка данных menu", e);
        }
    }

    public static String getPropertyMenu(String key) {
        return menu.getProperty(key);
    }
}
