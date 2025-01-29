package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import ru.rental.servic.util.ConnectionManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class BaseBd {

    @BeforeEach
    public void initBike() {
        final var path = BikeDaoTest.class.getClassLoader().getResource("initBd.sql").getPath();
        try (final var connection = ConnectionManager.getConnection();
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
        } catch (SQLException | FileNotFoundException e) {
            throw new IllegalStateException("Ошибка инициализации таблицы", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
