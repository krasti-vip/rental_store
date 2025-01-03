package ru.rental.servic.service;

import ru.rental.servic.dao.BikeDao;
import ru.rental.servic.dto.BikeDto;
import ru.rental.servic.model.Bike;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BikeService implements Service<BikeDto, Integer> {

    private final BikeDao bikeDao = new BikeDao();

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
    public Optional<BikeDto> get(Integer id) {
        final var maybeBike = bikeDao.get(id);

        if (maybeBike == null) {
            return Optional.empty();
        } else {
            return Optional.of(convertByDto(maybeBike));
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
    public Optional<BikeDto> update(Integer id, BikeDto obj) {
        var maybeBike = bikeDao.get(id);

        if (maybeBike == null) {
            return Optional.empty();
        }

        var updatedBike = Bike.builder()
                .id(maybeBike.getId())
                .name(obj.getName())
                .price(obj.getPrice())
                .volume(obj.getVolume())
                .horsePower(obj.getHorsePower())
                .build();

        var updated = bikeDao.update(id, updatedBike);
        return Optional.of(convertByDto(updated));
    }

    /**
     * метод который создает новый объект на основе переданого ему значения, проходит по массиву присваивает значения полям
     * на основе переданных производит добавления в массив и путем конвертации для дто
     * @param obj
     * @return
     */
    @Override
    public BikeDto save(BikeDto obj) {
        var newBike = Bike.builder()
                .name(obj.getName())
                .price(obj.getPrice())
                .volume(obj.getVolume())
                .horsePower(obj.getHorsePower())
                .build();

        var savedBike = bikeDao.save(newBike);

        return convertByDto(savedBike);
    }

    /**
     * метод который удаляет объект по id, проходим по массиву с помощью метода гет, если объекта нет(id) ничего не вернет,
     * иначе удалит объект по id
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        var maybeBike = bikeDao.get(id);

        if (maybeBike == null) {

            return false;
        }

        return bikeDao.delete(id);
    }

    /**
     * метод который фильтрует массив по переданому предикату, и возращает из дао все объекты пропущенные через фильтр,
     * стримом, уонвертированные в дто и сохраненые в новый лист
     * @param predicate
     * @return
     */
    @Override
    public List<BikeDto> filterBy(Predicate<BikeDto> predicate) {

        return bikeDao.getAll().stream()
                .map(bike -> this.convertByDto(bike))
                .filter(predicate)
                .toList();
    }

    /**
     * метод который передает весь список объектов, перевормотированные из дао в дто сохраненные в новом листе
     * @return
     */
    @Override
    public List<BikeDto> getAll() {
        return bikeDao.getAll().stream().map(bike -> convertByDto(bike)).toList(); //как и зачем тестировать
    }

    /**
     * метод который производит конвертацию объектов на основе указанных полей, в классе мы производим конвертацию из дао в дто
     * @param bike
     * @return
     */
    private BikeDto convertByDto(Bike bike) { //тот же вопрос
        return BikeDto.builder()
                .id(bike.getId())
                .name(bike.getName())
                .price(bike.getPrice())
                .volume(bike.getVolume())
                .horsePower(bike.getHorsePower())
                .build();
    }
}
