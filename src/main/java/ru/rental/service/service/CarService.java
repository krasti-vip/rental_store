package ru.rental.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dao.CarDao;
import ru.rental.service.dto.CarDto;
import ru.rental.service.model.Car;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class CarService implements Service<CarDto, Integer> {

    private static final Logger log = LoggerFactory.getLogger(CarService.class);

    private static final String NO_CAR_FOUND = "Care with id {} not found";

    private final CarDao carDao;

    @Autowired
    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    @Override
    public Optional<CarDto> get(Integer id) {
        final var maybeCar = carDao.get(id);

        if (maybeCar == null) {
            log.warn("Car with id {} not found", id);
            return Optional.empty();
        } else {
            log.info("Car with id {} found", id);
            return Optional.of(convertByDto(maybeCar));
        }
    }

    @Override
    public Optional<CarDto> update(Integer id, CarDto obj) {
        var maybeCar = carDao.get(id);

        if (maybeCar == null) {
            log.warn(NO_CAR_FOUND, id);
            return Optional.empty();
        }

        var updatedCar = Car.builder()
                .id(maybeCar.getId())
                .title(obj.getTitle())
                .price(obj.getPrice())
                .horsePower(obj.getHorsePower())
                .volume(obj.getVolume())
                .color(obj.getColor())
                .userId(obj.getUserId())
                .build();

        var updated = carDao.update(id, updatedCar);
        log.info("Car with id {} updated", id);
        return Optional.of(convertByDto(updated));
    }

    @Override
    public CarDto save(CarDto obj) {
        var newCar = Car.builder()
                .title(obj.getTitle())
                .price(obj.getPrice())
                .horsePower(obj.getHorsePower())
                .volume(obj.getVolume())
                .color(obj.getColor())
                .userId(obj.getUserId())
                .build();

        var savedCar = carDao.save(newCar);
        log.info("Car with id {} saved", savedCar.getId());
        return convertByDto(savedCar);
    }

    @Override
    public boolean delete(Integer id) {
        var maybeCar = carDao.get(id);

        if (maybeCar == null) {
            log.warn(NO_CAR_FOUND, id);
            return false;
        }
        log.info("Car with id {} deleted", id);
        return carDao.delete(id);
    }

    @Override
    public List<CarDto> filterBy(Predicate<CarDto> predicate) {
        log.info("filterBy");
        return carDao.getAll().stream()
                .map(this::convertByDto)
                .filter(predicate)
                .toList();
    }

    @Override
    public List<CarDto> getAll() {
        log.info("getAll");
        return carDao.getAll().stream().map(this::convertByDto).toList();
    }

    private CarDto convertByDto(Car car) {
        log.info("convertByDto");
        return CarDto.builder()
                .id(car.getId())
                .title(car.getTitle())
                .price(car.getPrice())
                .horsePower(car.getHorsePower())
                .volume(car.getVolume())
                .color(car.getColor())
                .userId(car.getUserId())
                .build();
    }

    public List<CarDto> getAllByUserId(int userId) {
        log.info("Fetching cars for user with id {}", userId);
        return carDao.getAllByUserId(userId).stream()
                .map(this::convertByDto)
                .toList();
    }

    public Optional<CarDto> updateUserId(Integer carId, Integer userId) {

        var updatedCar = carDao.updateUserId(carId, userId);

        if (updatedCar != null) {
            log.info("Car with id {} successfully updated with userId {}", carId, userId);
            return Optional.of(convertByDto(updatedCar));
        }

        log.warn("Car with id {} was not updated!", carId);
        return Optional.empty();
    }
}
