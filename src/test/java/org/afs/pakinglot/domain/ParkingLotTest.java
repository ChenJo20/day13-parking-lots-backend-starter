package org.afs.pakinglot.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.junit.jupiter.api.Test;

class ParkingLotTest {
    @Test
    void should_return_ticket_when_park_given_a_parking_lot_and_a_car() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        // When
        Ticket ticket = parkingLot.park(car);
        // Then
        assertNotNull(ticket);
    }

    @Test
    void should_return_car_when_fetch_given_a_parking_lot_and_a_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket = parkingLot.park(car);
        // When
        Car fetchedCar = parkingLot.fetch(ticket);
        // Then
        assertEquals(car, fetchedCar);
    }

    @Test
    void should_return_correct_car_when_fetch_twice_given_a_parking_lot_and_two_tickets() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car1 = new Car(CarPlateGenerator.generatePlate());
        Car car2 = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket1 = parkingLot.park(car1);
        Ticket ticket2 = parkingLot.park(car2);
        // When
        Car fetchedCar1 = parkingLot.fetch(ticket1);
        Car fetchedCar2 = parkingLot.fetch(ticket2);
        // Then
        assertEquals(car1, fetchedCar1);
        assertEquals(car2, fetchedCar2);
    }

    @Test
    void should_return_nothing_with_error_message_when_park_given_a_parking_lot_and_a_car_and_parking_lot_is_full() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        for (int i = 0; i < parkingLot.getCapacity(); i++) {
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
        }
        Car car = new Car(CarPlateGenerator.generatePlate());
        // When
        // Then
        NoAvailablePositionException exception =
            assertThrows(NoAvailablePositionException.class, () -> parkingLot.park(car));
        assertEquals("No available position.", exception.getMessage());
    }

    @Test
    void should_return_nothing_with_error_message_when_fetch_given_a_parking_lot_and_an_unrecognized_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Ticket unrecognizedTicket = new Ticket(CarPlateGenerator.generatePlate(), 1, 1, LocalDateTime.now());
        // When
        // Then
        UnrecognizedTicketException exception =
            assertThrows(UnrecognizedTicketException.class, () -> parkingLot.fetch(unrecognizedTicket));
        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_return_nothing_with_error_message_when_fetch_given_a_parking_lot_and_a_used_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket = parkingLot.park(car);
        parkingLot.fetch(ticket);
        // When
        // Then
        UnrecognizedTicketException exception =
            assertThrows(UnrecognizedTicketException.class, () -> parkingLot.fetch(ticket));
        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }


    @Test
    void should_return_ticks_list_when_getTickets_given_a_some_parked_cars() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car1 = new Car(CarPlateGenerator.generatePlate());
        Car car2 = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket1 = parkingLot.park(car1);
        Ticket ticket2 = parkingLot.park(car2);
        List<Ticket> expectedTickets = List.of(ticket1, ticket2);
        // When
        List<Ticket> tickets = parkingLot.getTickets();
        // Then
        assertNotNull(tickets);
        assertTrue(expectedTickets.containsAll(tickets));
    }

}
