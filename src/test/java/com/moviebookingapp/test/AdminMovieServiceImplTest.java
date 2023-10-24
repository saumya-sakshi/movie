package com.moviebookingapp.test;

import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.MovieTheatreId;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.repo.MovieRepo;
import com.moviebookingapp.repo.TicketRepo;
import com.moviebookingapp.serviceImpl.AdminMovieServiceImpl;
import com.moviebookingapp.serviceImpl.TicketBookingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepo movieRepo;

    @Mock
    private TicketRepo ticketRepo;

    @InjectMocks
    private TicketBookingServiceImpl ticketBookingService;

    @InjectMocks
    private AdminMovieServiceImpl movieService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteMovieById_Success() {
        // Arrange
        String movieName = "Movie1";
        String theatreName = "Theatre1";
        MovieTheatreId id = new MovieTheatreId(movieName, theatreName);

        Movie movie = new Movie(id, 50);


        when(movieRepo.findById_MovieNameAndId_TheatreName(movieName, theatreName)).thenReturn(movie);
        doNothing().when(movieRepo).deleteById(id);

        String result = movieService.deleteMovieById(movieName, theatreName);

        verify(movieRepo).deleteById(id);
        Assertions.assertEquals("record deleted successfully", result);
    }

    @Test
    void testDeleteMovieById_MovieNotFound() {
        String movieName = "Non-existent Movie";
        String theatreName = "Theatre 1";
        MovieTheatreId id = new MovieTheatreId(movieName, theatreName);

        when(movieRepo.findById(id)).thenReturn(java.util.Optional.empty());

        String result = movieService.deleteMovieById(movieName, theatreName);

        Assertions.assertNull(result);
        verify(movieRepo, never()).deleteById(any());
    }

    @Test
    void testGetAllBookedDetails() {

        Ticket ticket1 = new Ticket(1l,"movie1","theatre1",10, Arrays.asList("A1", "A2"));
        Ticket ticket2 = new Ticket(10l,"movie2","theatre2",10, Arrays.asList("A1", "A2"));
        List<Ticket> expectedBookedDetails = Arrays.asList(ticket1, ticket2);

        when(ticketRepo.findAll()).thenReturn(expectedBookedDetails);


        List<Ticket> actualBookedDetails = movieService.getAllBookedDetails();

        Assertions.assertEquals(expectedBookedDetails, actualBookedDetails);
    }

    @Test
    void getBookedTicketsCountByMovieNameTest(){
        String movieName = "movie1";
        int expectedValue = 1;
        when(ticketRepo.countByMovieName(movieName)).thenReturn(expectedValue);
        int actualValue = movieService.getBookedTicketsCountByMovieName(movieName);
        Assertions.assertEquals(expectedValue,actualValue);

    }

    @Test
    void updateTicketAvailabilityTest()
    {
        String movieName = "movie1";
        Movie movie = movieRepo.findById_MovieName(movieName);
        when(movieRepo.findById_MovieName(movieName)).thenReturn(movie);

    }

    @Test
    void testUpdateTicketAvailability_SoldOut() {
        // Arrange
        String movieName = "Movie1";
        Movie movie = new Movie();
        movie.setTotalTicketsAllotted(50);

        when(movieRepo.findById_MovieName(movieName)).thenReturn(movie);
        when(ticketRepo.countByMovieName(movieName)).thenReturn(50);

        // Act
        String result = movieService.updateTicketAvailability(movieName);

        // Assert
        verify(movieRepo).save(movie);
        Assertions.assertEquals("Status updated successfully !!!", result);
        Assertions.assertEquals("Sold out", movie.getTicketStatus());
    }

    @Test
    void testUpdateTicketAvailability_BookASAP() {
        // Arrange
        String movieName = "Movie2";
        Movie movie = new Movie();
        movie.setTotalTicketsAllotted(50);

        when(movieRepo.findById_MovieName(movieName)).thenReturn(movie);
        when(ticketRepo.countByMovieName(movieName)).thenReturn(30);

        // Act
        String result = movieService.updateTicketAvailability(movieName);

        // Assert
        verify(movieRepo).save(movie);
        Assertions.assertEquals("Status updated successfully !!!", result);
        Assertions.assertEquals("BOOK ASAP", movie.getTicketStatus());
    }

    @Test
    void testUpdateTicketAvailability_Available() {
        // Arrange
        String movieName = "Movie3";
        Movie movie = new Movie();
        movie.setTotalTicketsAllotted(50);

        when(movieRepo.findById_MovieName(movieName)).thenReturn(movie);
        when(ticketRepo.countByMovieName(movieName)).thenReturn(10);

        // Act
        String result = movieService.updateTicketAvailability(movieName);

        // Assert
        verify(movieRepo).save(movie);
        Assertions.assertEquals("Status updated successfully !!!", result);
        Assertions.assertEquals("Available", movie.getTicketStatus());
    }

    @Test
    void testUpdateTicketAvailability_MovieNotFound() {
        // Arrange
        String movieName = "Movie1";
        when(movieRepo.findById_MovieName(movieName)).thenReturn(null);

        // Act
        String result = movieService.updateTicketAvailability(movieName);

        // Assert
        Assertions.assertNull(result);
        verify(movieRepo).findById_MovieName(movieName);
        verifyNoMoreInteractions(movieRepo);
    }

}
