package ru.rental.service.service;

import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
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
            return Optional.of(modelMapper.map(maybeUser, UserDto.class));
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
        return Optional.of(modelMapper.map(updated, UserDto.class));
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
        return modelMapper.map(savedUser, UserDto.class);
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
                .map(e -> modelMapper.map(e, UserDto.class))
                .filter(predicate)
                .toList();
    }

    @Override
    public List<UserDto> getAll() {

        List<User> users = userDao.getAll();
        log.info("Users found");
        return users.stream()
                .map(e -> modelMapper.map(e, UserDto.class))
                .toList();
    }
}
