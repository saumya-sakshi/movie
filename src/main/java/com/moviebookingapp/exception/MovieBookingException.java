package com.moviebookingapp.exception;

public class MovieBookingException extends RuntimeException {

    public MovieBookingException(String message) {
        super(message);
    }

    public MovieBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}

