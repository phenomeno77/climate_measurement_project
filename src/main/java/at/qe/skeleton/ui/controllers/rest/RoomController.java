package at.qe.skeleton.ui.controllers.rest;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/room")

public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    MeasurementService measurementService;


    @PostMapping("/add")
    Room addRoom(@RequestBody Room room) {

        return roomService.saveRoom(room);
    }


    @GetMapping("/findAll")
    Collection<Room> findAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/delete")
    @ResponseBody
    void deleteRoom(){
        Room room = roomService.loadRoom(10);
        roomService.deleteRoom(room);
    }

}

