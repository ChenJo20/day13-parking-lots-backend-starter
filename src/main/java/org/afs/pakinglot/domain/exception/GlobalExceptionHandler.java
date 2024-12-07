package org.afs.pakinglot.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoAvailablePositionException.class)
    public ResponseEntity<String> handleNoAvailablePositionException(NoAvailablePositionException ex) {
        return new ResponseEntity<>("No available positions", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistPlateNumberException.class)
    public ResponseEntity<String> handleExistPlateNumberException(ExistPlateNumberException ex) {
        return new ResponseEntity<>("Plate Number exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnrecognizedTicketException.class)
    public ResponseEntity<String> handleUnrecognizedTicketException(UnrecognizedTicketException ex) {
        return new ResponseEntity<>("Unrecognized ticket", HttpStatus.BAD_REQUEST);
    }


}