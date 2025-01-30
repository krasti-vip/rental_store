package ru.rental.servic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.servic.dao.BikeDao;
import ru.rental.servic.dto.BikeDto;
import ru.rental.servic.model.Bike;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class BikeService implements Service<BikeDto, Integer> {

    private final BikeDao bikeDao;

    @Autowired
    public BikeService(BikeDao bikeDao) {
        this.bikeDao = bikeDao;
    }

    @Override
    public Optional<BikeDto> get(Integer id) {
        final var maybeBike = bikeDao.get(id);

        if (maybeBike == null) {
            return Optional.empty();
        } else {
            return Optional.of(convertByDto(maybeBike));
        }
    }

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

    @Override
    public boolean delete(Integer id) {
        var maybeBike = bikeDao.get(id);

        if (maybeBike == null) {

            return false;
        }

        return bikeDao.delete(id);
    }

    @Override
    public List<BikeDto> filterBy(Predicate<BikeDto> predicate) {

        return bikeDao.getAll().stream()
                .map(this::convertByDto)
                .filter(predicate)
                .toList();
    }

    @Override
    public List<BikeDto> getAll() {
        return bikeDao.getAll().stream().map(this::convertByDto).toList();
    }

    private BikeDto convertByDto(Bike bike) {
        return BikeDto.builder()
                .id(bike.getId())
                .name(bike.getName())
                .price(bike.getPrice())
                .volume(bike.getVolume())
                .horsePower(bike.getHorsePower())
                .build();
    }
}
