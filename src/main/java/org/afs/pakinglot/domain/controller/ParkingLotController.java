package org.afs.pakinglot.domain.controller;

import org.afs.pakinglot.criteria.ParkCriteria;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.dto.ParkingLotDTO;
import org.afs.pakinglot.domain.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parking-lots")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;


    @GetMapping
    public List<ParkingLotDTO> getParkingLots() {
        return parkingLotService.getParkingLots();
    }

    @PostMapping("/park")
    public Ticket park(@RequestBody ParkCriteria criteria) {
        return parkingLotService.parkCar(criteria);
    }
}