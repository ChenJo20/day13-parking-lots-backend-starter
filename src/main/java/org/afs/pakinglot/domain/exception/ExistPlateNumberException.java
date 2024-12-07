package org.afs.pakinglot.domain.exception;

public class ExistPlateNumberException extends RuntimeException {
    public ExistPlateNumberException() {
        super("Plate Number exists.");
    }
}