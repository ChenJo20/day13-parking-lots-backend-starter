package org.afs.pakinglot.domain;

import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;

import java.util.List;

public class ParkingLotManager {
    private final List<ParkingLot> parkingLots;
    private final List<ParkingBoy> parkingBoys;

    public ParkingLotManager() {
        this.parkingLots = List.of(
                new ParkingLot(1, "The Plaza Lot", 9),
                new ParkingLot(2, "City Mall Garage", 12),
                new ParkingLot(3, "Office Tower Parking", 9)
        );

        this.parkingBoys = List.of(
                new ParkingBoy(parkingLots, new SequentiallyStrategy()),
                new ParkingBoy(parkingLots, new MaxAvailableStrategy()),
                new ParkingBoy(parkingLots, new AvailableRateStrategy())
        );
    }

    public List<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public List<ParkingBoy> getParkingBoys() {
        return parkingBoys;
    }
}