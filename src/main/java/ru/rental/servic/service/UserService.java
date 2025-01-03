package ru.rental.servic.service;

import ru.rental.servic.dao.UserDao;
import ru.rental.servic.dto.UserDto;
import ru.rental.servic.model.Car;
import ru.rental.servic.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class UserService implements Service<UserDto, String> {

    private final UserDao userDao = new UserDao();

    /**
     * метод который позволяет осуществлять поиск нужного объекта по его id тут это его кличка,
     * @param id, метод защищен от налл путем обертки класса опшинл, запрос на состав полей объекта идет в класс дто,
     *            создает не изменяемый объект для прохода по массиву, для поиска нужного по его id, если созданный объект
     *            налл то выкидывает исключение, чтобы не упасть, иначе через метод конверт преобразовываем его в дто(что
     *           бы можно было безопасно использовать, без прямого взаимодействия с дао) и
     *            возвращаем объект
     * @return
     */
    @Override
    public Optional<UserDto> get(String id) {
        final var maybeUser = userDao.get(id);

        if (maybeUser == null) {
            return Optional.empty();
        } else {
            return Optional.of(convertByDto(maybeUser));
        }
    }

    /**
     * метод который производит обновления объекта путем поиска по его id, метод защищен от ошибки налл классом опшнл,
     * создаем объект на основе полей из дто и присваиваем значения из дао по id объекта, если предан пустой объект
     * выкинет исключение, иначе пройдем по массиву объектов с помощью библиотеки билдер, присвом им значение переданного
     * объекта, далее присвает объекту по id поля из билдера и возращает путем конвертации в дто новый объект
     * @param id
     * @param obj
     * @return
     */
    @Override
    public Optional<UserDto> update(String id, UserDto obj) {
        var maybeUser = userDao.get(id);

        if (maybeUser == null) {
            return Optional.empty();
        }

        var updatedUser = User.builder()
                .userName(maybeUser.getUserName())
                .firstName(obj.getFirstName())
                .lastName(obj.getLastName())
                .passport(obj.getPassport())
                .email(obj.getEmail())
                .bankCard(obj.getBankCard())
                .build();

        var updated = userDao.update(id, updatedUser);
        return Optional.of(convertByDto(updated));
    }

    /**
     * метод который создает новый объект на основе переданого ему значения, проходит по массиву присваивает значения полям
     * на основе переданных производит добавления в массив и путем конвертации для дто
     * @param obj
     * @return
     */
    @Override
    public UserDto save(UserDto obj) {
        var newUser = User.builder()
                .firstName(obj.getFirstName())
                .lastName(obj.getLastName())
                .passport(obj.getPassport())
                .email(obj.getEmail())
                .bankCard(obj.getBankCard())
                .build();

        var savedUser = userDao.save(newUser);

        return convertByDto(savedUser);
    }

    /**
     * метод который удаляет объект по id, проходим по массиву с помощью метода гет, если объекта нет(id) ничего не вернет,
     * иначе удалит объект по id
     * @param id
     * @return
     */
    @Override
    public boolean delete(String id) {
        var maybeUser = userDao.get(id);

        if (maybeUser == null) {

            return false;
        }

        return userDao.delete(id);
    }

    /**
     * метод который фильтрует массив по переданому предикату, и возращает из дао все объекты пропущенные через фильтр,
     * стримом, уонвертированные в дто и сохраненые в новый лист
     * @param predicate
     * @return
     */
    @Override
    public List<UserDto> filterBy(Predicate<UserDto> predicate) {

        return userDao.getAll().stream()
                .map(user -> this.convertByDto(user))
                .filter(predicate)
                .toList();
    }

    /**
     * метод который передает весь список объектов, перевормотированные из дао в дто сохраненные в новом листе
     * @return
     */
    @Override
    public List<UserDto> getAll() {
        return userDao.getAll().stream().map(user -> convertByDto(user)).toList();
    }

    /**
     * метод который производит конвертацию объектов на основе указанных полей, в классе мы производим конвертацию из дао в дто
     * @param
     * @return
     */
    private UserDto convertByDto(User user) {
        return UserDto.builder()
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .passport(user.getPassport())
                .email(user.getEmail())
                .bankCard(user.getBankCard())
                .build();
    }
}
