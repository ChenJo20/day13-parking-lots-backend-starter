package org.afs.pakinglot.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class FetchResult {
    private Car car;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fetchTime;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime parkDate;
    private long fee;


    @JsonCreator
    public FetchResult(@JsonProperty("car") Car car,
                       @JsonProperty("fetchTime") LocalDateTime fetchTime,
                       @JsonProperty("parkDate") LocalDateTime parkDate,
                       @JsonProperty("fee") long fee) {
        this.car = car;
        this.fetchTime = fetchTime;
        this.parkDate = parkDate;
        this.fee = fee;
    }

    // Getters and setters
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDateTime getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(LocalDateTime fetchTime) {
        this.fetchTime = fetchTime;
    }

    public LocalDateTime getParkDate() {
        return parkDate;
    }

    public void setParkDate(LocalDateTime parkDate) {
        this.parkDate = parkDate;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }
}