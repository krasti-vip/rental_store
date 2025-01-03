package ru.rental.servic.dao;

import ru.rental.servic.model.Bike;
import ru.rental.servic.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UserDao implements DAO<User, String> {

    private final List<User> bd = new ArrayList<>() {{
        add(User.builder()
                .userName("Падший Ангел")
                .firstName("Иванов")
                .lastName("Федя")
                .passport(328569)
                .email("angel@mail.com")
                .bankCard(7894)
                .build()
        );
        add(User.builder()
                .userName("Скелет")
                .firstName("Сидоров")
                .lastName("Петя")
                .passport(123456)
                .email("scelet@mail.com")
                .bankCard(5612)
                .build()
        );
        add(User.builder()
                .userName("Жонный Пит")
                .firstName("Петров")
                .lastName("Саша")
                .passport(456982)
                .email("pit@mail.com")
                .bankCard(4826)
                .build()
        );
        add(User.builder()
                .userName("Мёртвый Дэйв")
                .firstName("Теличко")
                .lastName("Кеша")
                .passport(458796)
                .email("dead@mail.com")
                .bankCard(6527)
                .build()
        );
        add(User.builder()
                .userName("Мальборо")
                .firstName("Бойко")
                .lastName("Валентин")
                .passport(741269)
                .email("malboro@mail.com")
                .bankCard(6214)
                .build()
        );
        add(User.builder()
                .userName("Дохлый шторм")
                .firstName("Лемачко")
                .lastName("Саша")
                .passport(564782)
                .email("skinny@mail.com")
                .bankCard(9167)
                .build()
        );
        add(User.builder()
                .userName("Мятое одеяло")
                .firstName("Ковальчук")
                .lastName("Филимон")
                .passport(951753)
                .email("crumpled@mail.com")
                .bankCard(6728)
                .build()
        );
        add(User.builder()
                .userName("Сломанный стол")
                .firstName("Рязанцев")
                .lastName("Артем")
                .passport(946873)
                .email("broken@mail.com")
                .bankCard(5547)
                .build()
        );
        add(User.builder()
                .userName("Синний глаз")
                .firstName("Полянский")
                .lastName("Дима")
                .passport(302108)
                .email("eye@mail.com")
                .bankCard(7850)
                .build()
        );
        add(User.builder()
                .userName("Орел пустыни")
                .firstName("Головка")
                .lastName("Артем")
                .passport(465801)
                .email("eagle@mail.com")
                .bankCard(9010)
                .build()
        );
    }};

    /**
     * метод который возращает нужный объект по запросу его userName(id), в нашем классе это его кличка, циклом фор эйч
     * пробигаем по массиву и перебираем каждый объект, как он нашел нужный, путем сравнения с запросом через иф
     * и возращает найденный, иначе ничего не вернет
     * @param id
     * @return
     */
    @Override
    public User get(String id) {
        for(User user : bd) {
            if(user.getUserName().equals(id)) {

                return user;
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
    public User update(String id, User obj) {
        for(int i = 0; i < bd.size(); i++) {
            User user = bd.get(i);
            if(user.getUserName().equals(id)) {
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
    public User save(User obj) {
        for (User user : bd) {
            if(user.getUserName().equals(obj.getUserName())) {

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
    public boolean delete(String id) {
        for(User user : bd) {
            if(user.getUserName().equals(id)) {
                bd.remove(user);

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
    public List<User> filterBy(Predicate<User> predicate) {

        return bd.stream().filter(predicate).toList();
    }

    /**
     * метод который просто возращает копию всего массива
     * @return
     */
    public List<User> getAll() {
        return new ArrayList<>(bd);
    }
}
