package ru.rental.servic.util;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static final Logger log = (Logger) LoggerFactory.getLogger(PropertiesUtil.class.getName());
    private static final Properties PROPERTIES = new Properties();

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
}
