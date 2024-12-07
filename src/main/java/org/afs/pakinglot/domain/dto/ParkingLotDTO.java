package org.afs.pakinglot.domain.dto;

import java.util.List;

public class ParkingLotDTO {

    private int id;
    private String name;
    private List<TicketDTO> tickets;

    public ParkingLotDTO(int id, String name, List<TicketDTO> tickets) {
        this.id = id;
        this.name = name;
        this.tickets = tickets;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

}