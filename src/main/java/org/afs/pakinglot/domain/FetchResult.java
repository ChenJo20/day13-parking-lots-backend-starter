package org.afs.pakinglot.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchResult {
    private Car car;
    private String fetchTime;
    private String parkDate;
    private long fee;
    private long hours;
    private long minutes;


    @JsonCreator
    public FetchResult(@JsonProperty("car") Car car,
                       @JsonProperty("fetchTime") String fetchTime,
                       @JsonProperty("parkDate") String parkDate,
                       @JsonProperty("fee") long fee, @JsonProperty("hours") long hours, @JsonProperty("minutes") long minutes) {
        this.car = car;
        this.fetchTime = fetchTime;
        this.parkDate = parkDate;
        this.fee = fee;
        this.hours = hours;
        this.minutes = minutes;
    }

    // Getters and setters
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public String getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(String fetchTime) {
        this.fetchTime = fetchTime;
    }

    public String getParkDate() {
        return parkDate;
    }

    public void setParkDate(String parkDate) {
        this.parkDate = parkDate;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }
}