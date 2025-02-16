package ru.rental.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dao.BikeDao;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.model.Bike;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class BikeService implements Service<BikeDto, Integer> {

    private static final Logger log = LoggerFactory.getLogger(BikeService.class);

    private static final String NO_BIKE_FOUND = "Bike with id {} not found";

    private final BikeDao bikeDao;

    @Autowired
    public BikeService(BikeDao bikeDao) {
        this.bikeDao = bikeDao;
    }

    @Override
    public Optional<BikeDto> get(Integer id) {
        final var maybeBike = bikeDao.get(id);

        if (maybeBike == null) {
            log.warn(NO_BIKE_FOUND, id);
            return Optional.empty();
        } else {
            log.info("Bike with id {} found", id);
            return Optional.of(convertByDto(maybeBike));
        }
    }

    @Override
    public Optional<BikeDto> update(Integer id, BikeDto obj) {
        var maybeBike = bikeDao.get(id);

        if (maybeBike == null) {
            log.warn(NO_BIKE_FOUND, id);
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
        log.info("Bike with id {} updated", id);
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
        log.info("Bike with id {} saved", savedBike.getId());
        return convertByDto(savedBike);
    }

    @Override
    public boolean delete(Integer id) {
        var maybeBike = bikeDao.get(id);

        if (maybeBike == null) {
            log.warn(NO_BIKE_FOUND, id);
            return false;
        }
        log.info("Bike with id {} deleted", id);
        return bikeDao.delete(id);
    }

    @Override
    public List<BikeDto> filterBy(Predicate<BikeDto> predicate) {
        log.info("Bike filtering by {}", predicate);
        return bikeDao.getAll().stream()
                .map(this::convertByDto)
                .filter(predicate)
                .toList();
    }

    @Override
    public List<BikeDto> getAll() {
        if (bikeDao.getAll().isEmpty()) {
            log.info("No Bikes");
            return new ArrayList<>();
        }
        log.info("Bikes found");
        return bikeDao.getAll().stream().map(this::convertByDto).toList();
    }

    private BikeDto convertByDto(Bike bike) {
        log.info("Bike with id {} converted", bike.getId());
        return BikeDto.builder()
                .id(bike.getId())
                .name(bike.getName())
                .price(bike.getPrice())
                .volume(bike.getVolume())
                .horsePower(bike.getHorsePower())
                .userId(bike.getUserId())
                .build();
    }

    public List<BikeDto> getAllByUserId(int userId) {
        log.info("Fetching bikes for user with id {}", userId);
        return bikeDao.getAllByUserId(userId).stream()
                .map(this::convertByDto)
                .toList();
    }

    public Optional<BikeDto> updateUserId(Integer bikeId, Integer userId) {

        var updatedBike = bikeDao.updateUserId(bikeId, userId);

        if (updatedBike != null) {
            log.info("Bike with id {} successfully updated with userId {}", bikeId, userId);
            return Optional.of(convertByDto(updatedBike));
        }

        log.warn("Bike with id {} was not updated!", bikeId);
        return Optional.empty();
    }
}
