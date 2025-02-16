package ru.rental.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dao.UserDao;
import ru.rental.service.dto.UserDto;
import ru.rental.service.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class UserService implements Service<UserDto, Integer> {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final String NO_USER_FOUND = "User not found";

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<UserDto> get(Integer id) {
        final var maybeUser = userDao.get(id);

        if (maybeUser == null) {
            log.info(NO_USER_FOUND);
            return Optional.empty();
        } else {
            log.info("User found");
            return Optional.of(convertByDto(maybeUser));
        }
    }

    @Override
    public Optional<UserDto> update(Integer id, UserDto obj) {
        var maybeUser = userDao.get(id);

        if (maybeUser == null) {
            log.info(NO_USER_FOUND);
            return Optional.empty();
        }

        var updatedUser = User.builder()
                .userName(obj.getUserName())
                .firstName(obj.getFirstName())
                .lastName(obj.getLastName())
                .passport(obj.getPassport())
                .email(obj.getEmail())
                .bankCard(obj.getBankCard())
                .listBike(obj.getListBike())
                .listCar(obj.getListCar())
                .build();

        var updated = userDao.update(id, updatedUser);
        log.info("User updated");
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
        log.info("User saved");
        return convertByDto(savedUser);
    }

    @Override
    public boolean delete(Integer id) {
        var maybeUser = userDao.get(id);

        if (maybeUser == null) {
            log.info(NO_USER_FOUND);
            return false;
        }
        log.info("User deleted");
        return userDao.delete(id);
    }

    @Override
    public List<UserDto> filterBy(Predicate<UserDto> predicate) {

        List<User> users = userDao.getAll();
        log.info("Users found");
        return users.stream()
                .map(this::convertByDto)
                .filter(predicate)
                .toList();
    }

    @Override
    public List<UserDto> getAll() {

        List<User> users = userDao.getAll();
        log.info("Users found");
        return users.stream()
                .map(this::convertByDto)
                .toList();
    }

    private UserDto convertByDto(User user) {
        log.info("Converting UserDto");
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
