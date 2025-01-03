package ru.rental.servic.service;

import ru.rental.servic.dao.CarDao;
import ru.rental.servic.dto.CarDto;
import ru.rental.servic.model.Bike;
import ru.rental.servic.model.Car;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CarService implements Service<CarDto, Integer> {

    // mvc(model, view, controller)
    // user (дай мне машину под id 3) google.com/get/car/3 -> servlet(google.com/get/car/3) -> service.get(3) <- (convert) -> dao.get(3) -> bd
    // dto (data, transfer, object)

    // (frontend)  <- (name(String), (double)price, attribute) | (/get/car/3) ->  (backend)
    private final CarDao carDao = new CarDao();

    /**
     * метод который позволяет осуществлять поиск нужного объекта по его id тут это его номер по порядку,
     * @param id, метод защищен от налл путем обертки класса опшинл, запрос на состав полей объекта идет в класс дто,
     *            создает не изменяемый объект для прохода по массиву, для поиска нужного по его id, если созданный объект
     *            налл то выкидывает исключение, чтобы не упасть, иначе через метод конверт преобразовываем его в дто(что
     *           бы можно было безопасно использовать, без прямого взаимодействия с дао) и
     *            возвращаем объект
     * @return
     */
    @Override
    public Optional<CarDto> get(Integer id) {
        final var maybeCar = carDao.get(id);

        if (maybeCar == null) {
            return Optional.empty();
        } else {
            return Optional.of(convertByDto(maybeCar));
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
    public Optional<CarDto> update(Integer id, CarDto obj) {
        var maybeCar = carDao.get(id);

        if (maybeCar == null) {
            return Optional.empty();
        }

        var updatedCar = Car.builder()
                .id(maybeCar.getId())
                .name(obj.getName())
                .price(obj.getPrice())
                .volume(obj.getVolume())
                .horsePower(obj.getHorsePower())
                .build();

        var updated = carDao.update(id, updatedCar);
        return Optional.of(convertByDto(updated));
    }

    /**
     * метод который создает новый объект на основе переданого ему значения, проходит по массиву присваивает значения полям
     * на основе переданных производит добавления в массив и путем конвертации для дто
     * @param obj
     * @return
     */
    @Override
    public CarDto save(CarDto obj) {
        var newCar = Car.builder()
                .name(obj.getName())
                .price(obj.getPrice())
                .volume(obj.getVolume())
                .horsePower(obj.getHorsePower())
                .build();

        var savedCar = carDao.save(newCar);

        return convertByDto(savedCar);
    }

    /**
     * метод который удаляет объект по id, проходим по массиву с помощью метода гет, если объекта нет(id) ничего не вернет,
     * иначе удалит объект по id
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        var maybeCar = carDao.get(id);

        if (maybeCar == null) {

            return false;
        }

        return carDao.delete(id);
    }

    /**
     * метод который фильтрует массив по переданому предикату, и возращает из дао все объекты пропущенные через фильтр,
     * стримом, уонвертированные в дто и сохраненые в новый лист
     * @param predicate
     * @return
     */
    @Override
    public List<CarDto> filterBy(Predicate<CarDto> predicate) {

        return carDao.getAll().stream()
                .map(car -> this.convertByDto(car))
                .filter(predicate)
                .toList();
    }

    /**
     * метод который передает весь список объектов, перевормотированные из дао в дто сохраненные в новом листе
     * @return
     */
    @Override
    public List<CarDto> getAll() {
        return carDao.getAll().stream().map(car -> convertByDto(car)).toList();
    }

    /**
     * метод который производит конвертацию объектов на основе указанных полей, в классе мы производим конвертацию из дао в дто
     * @param
     * @return
     */
    private CarDto convertByDto(Car car) {
        return CarDto.builder()
                .color(car.getColor())
                .name(car.getName())
                .price(car.getPrice())
                .volume(car.getVolume())
                .horsePower(car.getHorsePower())
                .build();
    }
}
