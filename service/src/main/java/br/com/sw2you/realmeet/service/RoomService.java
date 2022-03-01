package br.com.sw2you.realmeet.service;

import static java.util.Objects.requireNonNull;

import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public RoomDTO getRoom(@RequestBody Long id) {
        requireNonNull(id);
        Room room = roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
        return new RoomDTO().name(room.getName()).id(room.getId()).seats(room.getSeats());
    }

}
