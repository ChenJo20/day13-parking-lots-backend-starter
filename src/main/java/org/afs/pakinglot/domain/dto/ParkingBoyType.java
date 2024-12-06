package org.afs.pakinglot.domain.dto;

public enum ParkingBoyType {
    STANDARD("Standard"),
    SMART("Smart"),
    SUPER_SMART("SuperSmart");

    private final String type;

    ParkingBoyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ParkingBoyType fromType(String type) {
        for (ParkingBoyType parkingBoyType : values()) {
            if (parkingBoyType.getType().equalsIgnoreCase(type)) {
                return parkingBoyType;
            }
        }
        throw new IllegalArgumentException("Unknown parking boy type: " + type);
    }
}