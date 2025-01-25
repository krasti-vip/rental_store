package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import ru.rental.servic.util.PropertiesUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseTest {

    private static final String BD_URL = "db.url";
    private static final String BD_USERNAME = "db.username";
    private static final String BD_PASSWORD = "db.password";

    @BeforeEach
    public void initDb() {
        final var path = BikeDaoTest.class.getClassLoader().getResource("init.sql").getPath();
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var bufferedReader = new BufferedReader(new FileReader(path))) {
            var query = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().startsWith("--- ")) {
                    continue;
                }

                query.append(line).append(" ");
                if (line.trim().endsWith(";")) {
                    connection.createStatement().execute(query.toString());
                    query = new StringBuilder();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка инициализации таблицы", e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
