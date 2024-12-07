package org.afs.pakinglot.domain.mapper;

import org.afs.pakinglot.domain.ParkingLot;
import org.afs.pakinglot.domain.dto.ParkingLotDTO;
import org.afs.pakinglot.domain.dto.TicketDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ParkingLotMapper {

    public ParkingLotDTO toParkingLotDTO(ParkingLot parkingLot) {
        return new ParkingLotDTO(
                parkingLot.getId(),
                parkingLot.getName(),
                parkingLot.getTickets().stream()
                        .map(ticket -> new TicketDTO(ticket.position(), ticket.plateNumber(), ticket.parkingLot()))
                        .collect(Collectors.toList())
        );
    }
}