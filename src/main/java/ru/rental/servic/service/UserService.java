package ru.rental.servic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.servic.dao.UserDao;
import ru.rental.servic.dto.UserDto;
import ru.rental.servic.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class UserService implements Service<UserDto, Integer> {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<UserDto> get(Integer id) {
        final var maybeUser = userDao.get(id);

        if (maybeUser == null) {
            return Optional.empty();
        } else {
            return Optional.of(convertByDto(maybeUser));
        }
    }

    @Override
    public Optional<UserDto> update(Integer id, UserDto obj) {
        var maybeUser = userDao.get(id);

        if (maybeUser == null) {
            return Optional.empty();
        }

        var updatedUser = User.builder()
                .userName(obj.getUserName())
                .firstName(obj.getFirstName())
                .lastName(obj.getLastName())
                .passport(obj.getPassport())
                .email(obj.getEmail())
                .bankCard(obj.getBankCard())
                .build();

        var updated = userDao.update(id, updatedUser);
        return Optional.of(convertByDto(updated));
    }

    @Override
    public UserDto save(UserDto obj) {
        var newUser = User.builder()
                .userName(obj.getUserName())
                .firstName(obj.getFirstName())
                .lastName(obj.getLastName())
                .passport(obj.getPassport())
                .email(obj.getEmail())
                .bankCard(obj.getBankCard())
                .build();

        var savedUser = userDao.save(newUser);

        return convertByDto(savedUser);
    }

    @Override
    public boolean delete(Integer id) {
        var maybeUser = userDao.get(id);

        if (maybeUser == null) {

            return false;
        }

        return userDao.delete(id);
    }

    @Override
    public List<UserDto> filterBy(Predicate<UserDto> predicate) {

        List<User> users = userDao.getAll();

        return users.stream()
                .map(this::convertByDto)
                .filter(predicate)
                .toList();
    }

    @Override
    public List<UserDto> getAll() {

        List<User> users = userDao.getAll();

        return users.stream()
                .map(this::convertByDto)
                .toList();
    }

    private UserDto convertByDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .passport(user.getPassport())
                .email(user.getEmail())
                .bankCard(user.getBankCard())
                .build();
    }
}
