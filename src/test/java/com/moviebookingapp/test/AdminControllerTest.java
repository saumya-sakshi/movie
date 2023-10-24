package com.moviebookingapp.test;
import com.moviebookingapp.controller.AdminController;
import com.moviebookingapp.exception.MovieNotFoundException;
import com.moviebookingapp.exception.TicketStatusException;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.service.AdminMovieService;
import com.moviebookingapp.service.UserMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminControllerTest {



    @Mock
    private AdminMovieService adminMovieService;

    @Mock
    private UserMovieService userMovieService;

    @Mock
    private Logger logger;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }





    @Test
    @ExtendWith(MockitoExtension.class)
    public void testGetAllMoviesAdm() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie());
        movies.add(new Movie());

        when(userMovieService.getAllMovies()).thenReturn(movies);

        ResponseEntity<List<Movie>> response = adminController.getAllMoviesAdm();

        assertEquals(movies, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userMovieService, times(1)).getAllMovies();
    }


    @Test
    public void testDeleteMovieById_MovieNotFoundException() {
        String movieName = "Movie 1";
        String theatreName = "Theatre 1";

        when(adminMovieService.deleteMovieById(movieName, theatreName)).thenReturn(null);

        try {
            adminController.deleteMovieById(movieName, theatreName);
        } catch (MovieNotFoundException e) {
            assertEquals("The movie with name Movie 1 is not present !", e.getMessage());
        }

        verify(adminMovieService, times(1)).deleteMovieById(movieName, theatreName);
    }

    @Test
    public void testGetALLBookedTicketsByMovieName() {
        String movieName = "Movie 1";
        int bookedTicketsCount = 5;

        when(adminMovieService.getBookedTicketsCountByMovieName(movieName)).thenReturn(bookedTicketsCount);

        ResponseEntity<?> response = adminController.getALLBookedTicketsByMovieName(movieName);

        assertEquals(bookedTicketsCount, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminMovieService, times(1)).getBookedTicketsCountByMovieName(movieName);
    }

    @Test
    public void testUpdateTicketAvailability() {
        String movieName = "Movie 1";

        when(adminMovieService.updateTicketAvailability(movieName)).thenReturn("Available");

        ResponseEntity<String> response = adminController.updateTicketAvailability(movieName);

        assertEquals("Ticket availability updated successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminMovieService, times(1)).updateTicketAvailability(movieName);

    }

    @Test
    public void testUpdateTicketAvailability_TicketStatusException() {
        String movieName = "Movie 1";

        when(adminMovieService.updateTicketAvailability(movieName)).thenReturn(null);

        try {
            adminController.updateTicketAvailability(movieName);
        } catch (TicketStatusException e) {
            assertEquals("Ticket Booking Unsuccessful", e.getMessage());
        }

        verify(adminMovieService, times(1)).updateTicketAvailability(movieName);
    }
}
