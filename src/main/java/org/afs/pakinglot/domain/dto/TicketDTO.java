package org.afs.pakinglot.domain.dto;

public class TicketDTO {

    private int position;
    private String plateNumber;
    private int parkingLot;

    public TicketDTO(int position, String plateNumber, int parkingLot) {
        this.position = position;
        this.plateNumber = plateNumber;
        this.parkingLot = parkingLot;
    }

    // Getters and setters
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(int parkingLot) {
        this.parkingLot = parkingLot;
    }

}