package com.moviebookingapp.test;

import com.moviebookingapp.controller.UserController;
import com.moviebookingapp.dto.TicketDto;
import com.moviebookingapp.exception.MovieNotFoundException;
import com.moviebookingapp.exception.TicketNotCreatedException;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.MovieTheatreId;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.service.TicketBookingService;
import com.moviebookingapp.service.UserMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserMovieService userMovieService;

    @Mock
    private TicketBookingService ticketBookingService;

    @InjectMocks
    private UserController userController;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        Movie movie1 = new Movie(new MovieTheatreId("Movie 1", "theatre1"), 100,"Available");
        Movie movie2 = new Movie(new MovieTheatreId("Movie 2", "theatre2"), 100,"Available");
    }





    @Test
    void testGetMovieByNameNotFound() {
        String movieName = "Non-existing Movie";

        when(userMovieService.MovieByName(movieName)).thenReturn(null);

        assertThrows(MovieNotFoundException.class, () -> userController.getMovieByName(movieName));
        verify(userMovieService, times(1)).MovieByName(movieName);
    }

    @Test
    void testBookTicket() {
        TicketDto ticket = new TicketDto();
        Ticket t = new Ticket();

        when(ticketBookingService.bookTicket(ticket)).thenReturn(t);

        ResponseEntity<Ticket> response = userController.bookTicket(ticket);

        //assertEquals(ticket, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(ticketBookingService, times(1)).bookTicket(ticket);
    }

    @Test
    void testBookTicketNotCreated() {
        TicketDto ticket = new TicketDto();

        when(ticketBookingService.bookTicket(ticket)).thenReturn(null);

        assertThrows(TicketNotCreatedException.class, () -> userController.bookTicket(ticket));
        verify(ticketBookingService, times(1)).bookTicket(ticket);
    }

    @Test
    void testCancelTicket() {
        Long ticketId = 1L;
        int cancellationResult = 1;

        when(ticketBookingService.cancelTicket(ticketId)).thenReturn(cancellationResult);

        ResponseEntity<String> response = userController.cancelTicket(ticketId);

        assertEquals("The ticket with id " + cancellationResult + " is deleted successfully!", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ticketBookingService, times(1)).cancelTicket(ticketId);
    }

    @Test
    void testCancelTicketNotCreated() {
        Long ticketId = 1L;
        int cancellationResult = 0;

        when(ticketBookingService.cancelTicket(ticketId)).thenReturn(cancellationResult);

        assertThrows(TicketNotCreatedException.class, () -> userController.cancelTicket(ticketId));
        verify(ticketBookingService, times(1)).cancelTicket(ticketId);
    }
}
