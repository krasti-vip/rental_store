package ru.rental.servic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.servic.dao.CarDao;
import ru.rental.servic.dto.CarDto;
import ru.rental.servic.model.Car;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class CarService implements Service<CarDto, Integer> {

    private final CarDao carDao;

    @Autowired
    public CarService(final CarDao carDao) {
        this.carDao = carDao;
    }

    @Override
    public Optional<CarDto> get(Integer id) {
        final var maybeCar = carDao.get(id);

        if (maybeCar == null) {
            return Optional.empty();
        } else {
            return Optional.of(convertByDto(maybeCar));
        }
    }

    @Override
    public Optional<CarDto> update(Integer id, CarDto obj) {
        var maybeCar = carDao.get(id);

        if (maybeCar == null) {
            return Optional.empty();
        }

        var updatedCar = Car.builder()
                .id(maybeCar.getId())
                .title(obj.getTitle())
                .price(obj.getPrice())
                .horsePower(obj.getHorsePower())
                .volume(obj.getVolume())
                .color(obj.getColor())
                .build();

        var updated = carDao.update(id, updatedCar);
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
                .build();

        var savedCar = carDao.save(newCar);

        return convertByDto(savedCar);
    }

    @Override
    public boolean delete(Integer id) {
        var maybeCar = carDao.get(id);

        if (maybeCar == null) {

            return false;
        }

        return carDao.delete(id);
    }

    @Override
    public List<CarDto> filterBy(Predicate<CarDto> predicate) {

        return carDao.getAll().stream()
                .map(this::convertByDto)
                .filter(predicate)
                .toList();
    }

    @Override
    public List<CarDto> getAll() {
        return carDao.getAll().stream().map(this::convertByDto).toList();
    }

    private CarDto convertByDto(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .title(car.getTitle())
                .price(car.getPrice())
                .horsePower(car.getHorsePower())
                .volume(car.getVolume())
                .color(car.getColor())
                .build();
    }
}
