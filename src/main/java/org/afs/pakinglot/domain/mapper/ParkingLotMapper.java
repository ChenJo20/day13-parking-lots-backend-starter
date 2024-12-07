package org.afs.pakinglot.domain.mapper;

import org.afs.pakinglot.domain.ParkingLot;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.dto.ParkingLotDTO;
import org.afs.pakinglot.domain.dto.TicketDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParkingLotMapper {

    public ParkingLotDTO toParkingLotDTO(ParkingLot parkingLot) {
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (int i = 1; i <= parkingLot.getCapacity(); i++) {
            int finalI = i;
            Ticket ticket = parkingLot.getTickets().stream()
                    .filter(t -> t.position() == finalI)
                    .findFirst()
                    .orElse(null);
            if (ticket != null) {
                ticketDTOs.add(new TicketDTO(ticket.position(), ticket.plateNumber(), ticket.parkingLot()));
            } else {
                ticketDTOs.add(new TicketDTO(i, "", parkingLot.getId()));
            }
        }
        return new ParkingLotDTO(
                parkingLot.getId(),
                parkingLot.getName(),
                ticketDTOs
        );
    }
}