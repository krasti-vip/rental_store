package ru.rental.servic.dao;

import ru.rental.servic.model.Car;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CarDao implements DAO<Car, Integer> {

    private final List<Car> bd = new ArrayList<>() {{
        add(Car.builder()
                .id(1)
                .name("Порше")
                .price(1500)
                .horsePower(220)
                .volume(2.5)
                .color("Черный")
                .build()
        );
        add(Car.builder()
                .id(2)
                .name("Жигули")
                .price(200)
                .horsePower(50)
                .volume(1.2)
                .color("Белый")
                .build()
        );
        add(Car.builder()
                .id(3)
                .name("Волга")
                .price(300)
                .horsePower(75)
                .volume(1.8)
                .color("Красный")
                .build()
        );
        add(Car.builder()
                .id(4)
                .name("Мерседес")
                .price(700)
                .horsePower(150)
                .volume(2.7)
                .color("Синий")
                .build()
        );
        add(Car.builder()
                .id(5)
                .name("БМВ")
                .price(900)
                .horsePower(170)
                .volume(2.9)
                .color("Зеленый")
                .build()
        );
        add(Car.builder()
                .id(6)
                .name("Попель")
                .price(450)
                .horsePower(110)
                .volume(3.0)
                .color("Золотой")
                .build()
        );
        add(Car.builder()
                .id(7)
                .name("Мазерати")
                .price(1700)
                .horsePower(180)
                .volume(4.5)
                .color("Фиолетовый")
                .build()
        );
        add(Car.builder()
                .id(8)
                .name("Тойота")
                .price(730)
                .horsePower(165)
                .volume(3.0)
                .color("Оранжевый")
                .build()
        );
        add(Car.builder()
                .id(9)
                .name("Феррари")
                .price(950)
                .horsePower(250)
                .volume(5.5)
                .color("Розовый")
                .build()
        );
        add(Car.builder()
                .id(10)
                .name("УАЗ")
                .price(150)
                .horsePower(55)
                .volume(2.0)
                .color("Черный")
                .build()
        );
    }};

    /**
     * метод который возращает нужный объект по запросу его id, в нашем классе это его номер по порядку, циклом фор эйч
     * пробигаем по массиву и перебираем каждый объект, как он нашел нужный, путем сравнения с запросом через иф
     * и возращает найденный, иначе ничего не вернет
     * @param id
     * @return
     */
    @Override
    public Car get(Integer id) {
        for (Car car : bd) {
            if(car.getId() == id) {

                return car;
            }
        }

        return null;
    }

    /**
     * метод котороый обновляет объект, путем поиска через метод гет, береберает массив через цикл фор, для поиска нужного
     * индекса, когда он найден, присваивает его объекту поиска, если у этого объекта такой же индекс который мы искали,
     * заменяем его на объект который передали методу через сет, если индекс объекта не найден то ничего не меняет потому что
     * вернем налл
     * @param id
     * @param obj
     * @return
     */
    @Override
    public Car update(Integer id, Car obj) {
        for(int i = 0; i < bd.size(); i++) {
            Car car = bd.get(i);
            if(car.getId() == id) {
                bd.set(i, obj);

                return obj;
            }
        }

        return null;
    }

    /**
     * метод который сохраняет новый объект, проходим по массиву циклом фор, если объект с таким индексом найден,
     * ничего не сохраняет и возращает налл, иначе добавляет новый объект
     * @param obj
     * @return
     */
    @Override
    public Car save(Car obj) {
        for(Car car : bd) {
            if(car.getId() == obj.getId()) {

                return null;
            }
        }
        bd.add(obj);

        return obj;
    }

    /**
     * метод который удаляет объект, проходит по массиву для поиска объекта по индексу, если индекс найден удаляет объект
     * передает тру что объект найден и удален, если индекса нет то вернет фолс что объект не удален
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        for(int i = 0; i < bd.size(); i++) {
            Car car = bd.get(i);
            if(car.getId() == id) {
                bd.remove(i);

                return true;
            }
        }

        return false;
    }

    /**
     * метод который фильтрует объект с помощью фильтра который нужно передать в предикат, запускается стрим, лист фильтруется
     * переданым предикатом, и сохраняется в новый лист
     * @param predicate
     * @return
     */
    @Override
    public List<Car> filterBy(Predicate<Car> predicate) {

        return bd.stream().filter(predicate).toList();
    }

    /**
     * метод который просто возращает копию всего массива
     * @return
     */
    public List<Car> getAll() {

        return bd;
    }
}
