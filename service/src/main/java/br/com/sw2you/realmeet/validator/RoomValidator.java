package br.com.sw2you.realmeet.validator;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2you.realmeet.validator.ValidatorUtils.*;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {
    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO createRoomDTO) {
        var vallidationErrors = new ValidationErrors();

        if (
            validateName(createRoomDTO.getName(), vallidationErrors) &&
            validateSeats(createRoomDTO.getSeats(), vallidationErrors)
        ) {
            validateNameDuplicate(createRoomDTO.getName(), vallidationErrors);
        }

        throwOnError(vallidationErrors);
    }

    private boolean validateName(String name, ValidationErrors vallidationErrors) {
        return (
            validateRequired(name, ROOM_NAME, vallidationErrors) &&
            validateMaxLength(name, ROOM_NAME, ROOM_NAME_MAX_LENGTH, vallidationErrors)
        );
    }

    private boolean validateSeats(Integer seats, ValidationErrors vallidationErrors) {
        return (
            validateRequired(seats, ROOM_SEATS, vallidationErrors) &&
            validateMinValue(seats, ROOM_SEATS, ROOM_SEATS_MIN_VALUE, vallidationErrors) &&
            validateMaxValue(seats, ROOM_SEATS, ROOM_SEATS_MAX_VALUE, vallidationErrors)
        );
    }

    private void validateNameDuplicate(String name, ValidationErrors vallidationErrors) {
        roomRepository
            .findByNameAndActive(name, true)
            .ifPresent(__ -> vallidationErrors.add(ROOM_NAME, ROOM_NAME + DUPLICATE));
    }
}
