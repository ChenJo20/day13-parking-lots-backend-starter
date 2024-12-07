package org.afs.pakinglot.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONUtil;
import org.afs.pakinglot.criteria.ParkCriteria;
import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotManager;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.dto.ParkingLotDTO;
import org.afs.pakinglot.domain.service.ParkingLotService;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    @Autowired
    private JacksonTester<Ticket> ticketJacksonTester;


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

    @Test
    public void shouldParkCarInSecondParkingLot_whenPark_givenSmartParkingBoy() throws Exception {
        // Given
        ParkCriteria criteria = new ParkCriteria();
        criteria.setPlateNumber("CC-9012");
        criteria.setParkingBoy("Smart"); // Assuming "Standard" parking boy parks in the second parking lot

        // When

        final String parkResponse = mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(criteria)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Ticket ticket = ticketJacksonTester.parseObject(parkResponse);

        // Then
        assertThat(ticket).isNotNull();
        assertThat(ticket.plateNumber()).isEqualTo("CC-9012");
        assertThat(ticket.parkingLot()).isEqualTo(2);

        // Verify the car is parked in the second parking lot
        List<ParkingLotDTO> parkingLots = parkingLotService.getParkingLots();
        ParkingLotDTO secondParkingLot = parkingLots.stream()
                .filter(lot -> lot.getId() == 2)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Second parking lot not found"));
        assertThat(secondParkingLot.getTickets())
                .extracting("plateNumber")
                .contains("CC-9012");
    }


    @ParameterizedTest
    @ValueSource(strings = {"Standard", "Smart", "SuperSmart"})
    public void shouldParkCarAndReturnTicket_whenPark_givenParkingBoyAndPlateNumber(String parkingBoy) throws Exception {
        // Given
        ParkCriteria criteria = new ParkCriteria();
        criteria.setPlateNumber("CC-9012");
        criteria.setParkingBoy(parkingBoy);

        // When
        final String parkResponse = mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(criteria)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Ticket ticket = ticketJacksonTester.parseObject(parkResponse);

        // Then
        assertThat(ticket).isNotNull();
        assertThat(ticket.plateNumber()).isEqualTo("CC-9012");
    }
}