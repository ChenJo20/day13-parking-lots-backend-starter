package org.afs.pakinglot.domain.controller;

import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotManager;
import org.afs.pakinglot.domain.dto.ParkingLotDTO;
import org.afs.pakinglot.domain.service.ParkingLotService;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ParkingLotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingLotManager parkingLotManager;

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private JacksonTester<List<ParkingLotDTO>> parkingLotDtoJacksonTester;


    @BeforeEach
    public void setup() {
        // Add specific cars to the first parking lot
        Car car1 = new Car("AA-1234");
        Car car2 = new Car("BB-5678");
        parkingLotManager.getParkingLots().get(0).park(car1);
        parkingLotManager.getParkingLots().get(0).park(car2);

    }

    @Test
    public void shouldReturnParkingLotsWithTickets_whenGetParkingLots_givenLot1HaveTwoCars() throws Exception {
        // Given
        List<ParkingLotDTO> givenDtos = parkingLotService.getParkingLots();

        // When & Then
        final String jsonResponse = mockMvc.perform(get("/parking-lots"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        final List<ParkingLotDTO> parkingLotDtoResult = parkingLotDtoJacksonTester.parseObject(jsonResponse);
        assertThat(parkingLotDtoResult)
                .usingRecursiveFieldByFieldElementComparator(
                        RecursiveComparisonConfiguration.builder()
                                .withComparedFields("id", "name", "tickets")
                                .build()
                )
                .isEqualTo(givenDtos);
    }
}