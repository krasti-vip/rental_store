package ru.rental.servic.dao;



import ru.rental.servic.model.Bike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BikeDao implements DAO<Bike, Integer> {

    private final List<Bike> bd = new ArrayList<>() {{
        add(Bike.builder()
                .id(1)
                .name("Ямаха")
                .price(777)
                .horsePower(45)
                .volume(0.750)
                .build()
        );
        add(Bike.builder()
                .id(2)
                .name("Хонда")
                .price(775)
                .horsePower(42)
                .volume(0.700)
                .build()
        );
        add(Bike.builder()
                .id(3)
                .name("Дукати")
                .price(770)
                .horsePower(78)
                .volume(1.000)
                .build()
        );
        add(Bike.builder()
                .id(4)
                .name("Харли Дэвидсон")
                .price(1250)
                .horsePower(101)
                .volume(2.300)
                .build()
        );
        add(Bike.builder()
                .id(5)
                .name("Кавасаки")
                .price(500)
                .horsePower(35)
                .volume(0.350)
                .build()
        );
        add(Bike.builder()
                .id(6)
                .name("Ямаха GT")
                .price(1000)
                .horsePower(70)
                .volume(1.000)
                .build()
        );
        add(Bike.builder()
                .id(7)
                .name("Хонда GTI")
                .price(1100)
                .horsePower(85)
                .volume(0.900)
                .build()
        );
        add(Bike.builder()
                .id(8)
                .name("Дукати super Hrust")
                .price(1800)
                .horsePower(95)
                .volume(1.500)
                .build()
        );
        add(Bike.builder()
                .id(9)
                .name("Подержанный Харли Дэвидсон")
                .price(650)
                .horsePower(101)
                .volume(2.300)
                .build()
        );
        add(Bike.builder()
                .id(10)
                .name("Кавасаки битый")
                .price(250)
                .horsePower(35)
                .volume(0.350)
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
    public Bike get(Integer id) {
        for(Bike bike : bd) {
            if(bike.getId() == id) {

                return bike;
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
    public Bike update(Integer id, Bike obj) {
        for(int i = 0; i < bd.size(); i++) {
            Bike bike = bd.get(i);
            if(bike.getId() == id) {
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
    public Bike save(Bike obj) {
        for (Bike bike : bd) {
            if(bike.getId() == obj.getId()) {
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
            Bike bike = bd.get(i);
            if(bike.getId() == id) {
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
    public List<Bike> filterBy(Predicate<Bike> predicate) {

        return bd.stream().filter(predicate).toList();
    }

    /**
     * метод который просто возращает копию всего массива
     * @return
     */
    public List<Bike> getAll() {

        return bd;
    }
//    @Override
//    public List<Bike> filterBy(Predicate<Bike> predicate) {
//        return bd.stream().filter(bike -> bike.getId() % 2 == 0).toList();
//    }
}
