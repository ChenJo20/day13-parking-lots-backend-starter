package org.afs.pakinglot.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ParkingLotManagerTest {


    @Test
    void should_initialize_when_construct_manager_given_parkinglots_parkingboys() {
        // Given
        ParkingLotManager manager = new ParkingLotManager();

        // When
        List<ParkingLot> parkingLots = manager.getParkingLots();
        List<ParkingBoy> parkingBoys = manager.getParkingBoys();

        // Then
        assertNotNull(parkingLots);
        assertNotNull(parkingBoys);
        assertEquals(3, parkingLots.size());
        assertEquals(3, parkingBoys.size());
    }

}