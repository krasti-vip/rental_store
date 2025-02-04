package ru.rental.service.dao;

import java.util.List;
import java.util.function.Predicate;

public interface DAO<T, I> {

    /**
     * Метод создания таблицы в базе данных, пока распространен на BikeDao, UserDao, CarDao
     * проверка на уникальность таблицы отсутствует, возможно дублирование, если по какой-то причине таблица не будет
     * создана выкинет эксэпшн IllegalStateException("Ошибка создания таблицы", e);
     */
    void createTable();

    /**
     * Метод возвращает объект User, Bike, Car по его id, присутствует проверка на null переданного id, в этом
     * случае бросит ошибку IllegalArgumentException("ID obj не может быть null"), если id существует, но
     * метод не смог вернуть его то бросит ошибку IllegalStateException("Ошибка передачи obj", e);
     *
     * @param id
     * @return
     */
    T get(I id);

    /**
     * Метод обновляет объект User, Bike, Car по переданному id и новому объекту для обновления, отсутствует проверка
     * на null для id или переданному объекту (могут быть проблемы), если обновление по другим причинам не произошло,
     * бросит exception IllegalStateException("Ошибка обновления obj", e);
     *
     * @param id
     * @param obj
     * @return
     */
    T update(I id, T obj);

    /**
     * Метод сохраняет новый переданный объект User, Bike, Car, отсутствует проверка на null (осторожней),
     * если по другой причине не сохранится obj, кинет исключение IllegalStateException("Ошибка сохранения obj", e);
     *
     * @param obj
     * @return
     */
    T save(T obj);

    /**
     * Метод удаляет объект User, Bike, Car по переданному id, отсутствует проверка на null (осторожней),
     * в другом случае если удаление не удалось, кинет исключение IllegalStateException("Ошибка удаления obj", e);
     *
     * @param id
     * @return
     */
    boolean delete(I id);

    /**
     * Метод осуществляет фильтрацию всех объектов находящихся в базе данных по переданному предикату и возвращает лист
     * с объектами удовлетворяющими критерии фильтрации, отсутствует проверка на null
     *
     * @param predicate
     * @return
     */
    List<T> filterBy(Predicate<T> predicate);

    /**
     * Метод возвращающий все объекты классов User, Bike, Car которые хранятся в базе данных, если передача не удалась
     * кинет ошибку IllegalStateException("Ошибка передачи всех obj", e);
     *
     * @return
     */
    List<T> getAll();

    /**
     * Метод проверяет по переданному названию таблицы, ее существование, вернет или True, или False
     *
     * @param tableName
     * @return
     */
    boolean checkIfTableExists(String tableName);
}
