package org.afs.pakinglot.domain.service;

import org.afs.pakinglot.criteria.ParkAndFetchCriteria;
import org.afs.pakinglot.domain.*;
import org.afs.pakinglot.domain.dto.ParkingBoyType;
import org.afs.pakinglot.domain.dto.ParkingLotDTO;
import org.afs.pakinglot.domain.exception.ExistPlateNumberException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.mapper.ParkingLotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotManager parkingLotManager;

    @Autowired
    private ParkingLotMapper parkingLotMapper;

    public List<ParkingLotDTO> getParkingLots() {
        return parkingLotManager.getParkingLots().stream()
                .map(parkingLotMapper::toParkingLotDTO)
                .collect(Collectors.toList());
    }

    public Ticket parkCar(ParkAndFetchCriteria criteria) {
        Car car = new Car(criteria.getPlateNumber());
        ParkingBoyType parkingBoyType = ParkingBoyType.fromType(criteria.getParkingBoy());
        checkPlateNumberExistence(criteria);

        ParkingBoy parkingBoy = switch (parkingBoyType) {
            case SMART -> parkingLotManager.getParkingBoys().get(1);
            case SUPER_SMART -> parkingLotManager.getParkingBoys().get(2);
            default -> parkingLotManager.getParkingBoys().get(0);
        };

        return parkingBoy.park(car);
    }

    private void checkPlateNumberExistence(ParkAndFetchCriteria criteria) {
        String plateNumber = criteria.getPlateNumber();
        long count = parkingLotManager.getParkingLots().stream()
                .filter(parkingLot -> parkingLot.getTickets().stream()
                        .anyMatch(ticket -> ticket.plateNumber().equals(plateNumber)))
                .count();
        if (count >= 1) {
            throw new ExistPlateNumberException();
        }
    }

    public Car fetchCar(String plateNumber) {
        System.out.println(plateNumber);
        List<ParkingLot> parkingLots = parkingLotManager.getParkingLots();
        for (ParkingLot parkingLot : parkingLots) {
            for (Ticket ticket : parkingLot.getTickets()) {
                System.out.println(ticket.plateNumber());
                if (ticket.plateNumber().equals(plateNumber)) {
                    return parkingLot.fetch(ticket);
                }
            }
        }
        throw new UnrecognizedTicketException();
    }
}