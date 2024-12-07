package org.afs.pakinglot.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.afs.pakinglot.criteria.ParkAndFetchCriteria;
import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.FetchResult;
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
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        parkingLotManager = new ParkingLotManager();
        // Add specific cars to the first parking lot
        Car car1 = new Car("AA-1234");
        Car car2 = new Car("BB-5678");
        parkingLotManager.getParkingLots().get(0).park(car1);
        parkingLotManager.getParkingLots().get(0).park(car2);

        // Inject the new ParkingLotManager instance into ParkingLotService
        Field managerField = ParkingLotService.class.getDeclaredField("parkingLotManager");
        managerField.setAccessible(true);
        managerField.set(parkingLotService, parkingLotManager);

    }

    @Test
    public void shouldReturnParkingLotsWithTickets_whenGetParkingLots_givenLot1HaveTwoCars() throws Exception {
        // Given
        List<ParkingLotDTO> givenDtos = parkingLotService.getParkingLots();

        // When & Then
        final String jsonResponse = mockMvc.perform(get("/parking-lots"))
                .andExpect(status().isOk())
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
        ParkAndFetchCriteria criteria = new ParkAndFetchCriteria();
        criteria.setPlateNumber("CC-9012");
        criteria.setParkingBoy("Smart"); // Assuming "Standard" parking boy parks in the second parking lot

        // When

        final String parkResponse = mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(criteria)))
                .andExpect(status().isOk())
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
        ParkAndFetchCriteria criteria = new ParkAndFetchCriteria();
        criteria.setPlateNumber("CC-9012");
        criteria.setParkingBoy(parkingBoy);

        // When
        final String parkResponse = mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(criteria)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Ticket ticket = ticketJacksonTester.parseObject(parkResponse);

        // Then
        assertThat(ticket).isNotNull();
        assertThat(ticket.plateNumber()).isEqualTo("CC-9012");
    }


    @Test
    public void shouldFetchCarFromParkingLot_whenFetch_givenPlateNumber() throws Exception {
        // Given
        String plateNumber = "CC-9012";
        ParkAndFetchCriteria parkAndFetchCriteria = new ParkAndFetchCriteria();
        parkAndFetchCriteria.setPlateNumber(plateNumber);
        parkAndFetchCriteria.setParkingBoy("Standard");
        mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(parkAndFetchCriteria)))
                .andExpect(status().isOk());

        // When
        MvcResult fetchResult = mockMvc.perform(post("/parking-lots/fetch")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(parkAndFetchCriteria)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String fetchResponse = fetchResult.getResponse().getContentAsString();
        FetchResult fetchResultObj = new ObjectMapper().readValue(fetchResponse, FetchResult.class);
        Car fetchedCar = fetchResultObj.getCar();
        assertThat(fetchedCar).isNotNull();
        assertThat(fetchedCar.plateNumber()).isEqualTo(plateNumber);

        // Verify the car is removed from the parking lot
        List<ParkingLotDTO> parkingLots = parkingLotService.getParkingLots();
        boolean carExists = parkingLots.stream()
                .flatMap(lot -> lot.getTickets().stream())
                .anyMatch(ticket -> ticket.getPlateNumber().equals(plateNumber));
        assertThat(carExists).isFalse();
    }

    @Test
    public void shouldThrowUnrecognizedTicketException_whenFetch_givenInvalidPlateNumber() throws Exception {
        // Given
        ParkAndFetchCriteria parkAndFetchCriteria = new ParkAndFetchCriteria();
        parkAndFetchCriteria.setPlateNumber("ZZ-9999");

        // When & Then
        mockMvc.perform(post("/parking-lots/fetch")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(parkAndFetchCriteria)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo("Unrecognized ticket"));
    }


    @Test
    public void shouldThrowNoAvailablePositionException_whenPark_givenNoAvailableSpots() throws Exception {
        // Given
        ParkAndFetchCriteria criteria = new ParkAndFetchCriteria();
        criteria.setPlateNumber("DD-3456");
        criteria.setParkingBoy("Standard");

        // Fill all parking spots
        parkingLotManager.getParkingLots().forEach(parkingLot -> {
            while (!parkingLot.isFull()) {
                parkingLot.park(new Car("XX-" + System.currentTimeMillis()));
            }
        });

        // When & Then
        mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(criteria)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo("No available positions"));
    }

    @Test
    public void shouldThrowExistPlateNumberException_whenPark_givenDuplicatePlateNumber() throws Exception {
        // Given
        ParkAndFetchCriteria criteria = new ParkAndFetchCriteria();
        criteria.setPlateNumber("DD-3456");
        criteria.setParkingBoy("Standard");

        // Park the car for the first time
        mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(criteria)))
                .andExpect(status().isOk());

        // When & Then - Attempt to park the same car again
        mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(criteria)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo("Plate Number exists"));
    }

    @Test
    public void shouldCalculateFeeCorrectly_whenFetchCar_givenParkDuration() throws Exception {
        // Given
        ParkAndFetchCriteria criteria = new ParkAndFetchCriteria();
        criteria.setPlateNumber("FF-1234");
        criteria.setParkingBoy("Standard");

        MvcResult parkResult = mockMvc.perform(post("/parking-lots/park")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(criteria)))
                .andExpect(status().isOk())
                .andReturn();
        Ticket ticket = new ObjectMapper().readValue(parkResult.getResponse().getContentAsString(), Ticket.class);

        LocalDateTime parkDate = LocalDateTime.of(2023, 10, 1, 10, 0);
        ticket = new Ticket(ticket.plateNumber(), ticket.position(), ticket.parkingLot(), parkDate);

        // when
        FetchResult fetchResult = parkingLotService.fetchCar(ticket.plateNumber());

        // Then
        assertThat(fetchResult.getFee()).isNotNull();
        assertThat(fetchResult.getFetchTime()).isNotNull();
        assertThat(fetchResult.getParkDate()).isNotNull();
    }
}