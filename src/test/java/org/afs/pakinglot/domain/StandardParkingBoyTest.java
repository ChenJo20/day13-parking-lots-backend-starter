package org.afs.pakinglot.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.junit.jupiter.api.Test;

class StandardParkingBoyTest {
    @Test
    void should_return_ticket_when_parking_given_a_parking_lot_and_a_car() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        // When
        Ticket ticket = parkingBoy.park(car);
        // Then
        assertNotNull(ticket);
    }

    @Test
    void should_return_the_car_when_fetch_given_a_parking_lot_and_a_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Ticket ticket = parkingBoy.park(car);
        // When
        Car fetchedCar = parkingBoy.fetch(ticket);
        // Then
        assertEquals(car, fetchedCar);
    }

    @Test
    void should_return_the_right_car_when_fetch_twice_given_a_parking_lot_with_two_parked_cars_and_two_tickets() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car1 = new Car(CarPlateGenerator.generatePlate());
        Car car2 = new Car(CarPlateGenerator.generatePlate());
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Ticket ticket1 = parkingBoy.park(car1);
        Ticket ticket2 = parkingBoy.park(car2);
        // When
        Car fetchedCar1 = parkingBoy.fetch(ticket1);
        Car fetchedCar2 = parkingBoy.fetch(ticket2);
        // Then
        assertEquals(car1, fetchedCar1);
        assertEquals(car2, fetchedCar2);
    }

    @Test
    void should_return_nothing_with_error_message_when_parking_given_a_wrong_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Ticket wrongTicket = new Ticket(CarPlateGenerator.generatePlate(), 1, 1, LocalDateTime.now());
        // When
        // Then
        UnrecognizedTicketException exception =
            assertThrows(UnrecognizedTicketException.class, () -> parkingBoy.fetch(wrongTicket));
        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_return_nothing_with_error_message_when_parking_given_a_used_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Ticket ticket = parkingBoy.park(car);
        parkingBoy.fetch(ticket);
        // When
        // Then
        UnrecognizedTicketException exception =
            assertThrows(UnrecognizedTicketException.class, () -> parkingBoy.fetch(ticket));
        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_return_nothing_with_error_message_when_parking_given_a_parking_lot_and_a_car_and_parking_lot_is_full() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        for (int i = 0; i < parkingLot.getCapacity(); i++) {
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
        }
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car(CarPlateGenerator.generatePlate());
        // When
        // Then
        NoAvailablePositionException exception =
            assertThrows(NoAvailablePositionException.class, () -> parkingBoy.park(car));
        assertEquals("No available position.", exception.getMessage());
    }

    @Test
    void should_park_to_the_first_parking_lot_when_park_given_two_available_parking_lots() {
        // Given
        ParkingLot firstParkingLot = new ParkingLot();
        ParkingLot secondParkingLot = new ParkingLot();
        List<ParkingLot> parkingLots = List.of(firstParkingLot, secondParkingLot);
        ParkingBoy parkingBoy = new ParkingBoy(parkingLots);
        Car car = new Car(CarPlateGenerator.generatePlate());
        // When
        Ticket ticket = parkingBoy.park(car);
        // Then
        assertNotNull(firstParkingLot.fetch(ticket));
    }

    @Test
    void should_park_to_the_second_parking_lot_when_park_given_the_first_parking_lot_is_full_and_the_second_parking_lot_is_available() {
        // Given
        ParkingLot firstParkingLot = new ParkingLot();
        for (int i = 0; i < firstParkingLot.getCapacity(); i++) {
            firstParkingLot.park(new Car(CarPlateGenerator.generatePlate()));
        }
        ParkingLot secondParkingLot = new ParkingLot();
        List<ParkingLot> parkingLots = List.of(firstParkingLot, secondParkingLot);
        ParkingBoy parkingBoy = new ParkingBoy(parkingLots);
        Car car = new Car(CarPlateGenerator.generatePlate());
        // When
        Ticket ticket = parkingBoy.park(car);
        // Then
        assertThrows(UnrecognizedTicketException.class, () -> firstParkingLot.fetch(ticket));
        assertNotNull(secondParkingLot.fetch(ticket));
    }
}
