package com.moviebookingapp.test;

import com.moviebookingapp.dto.TicketDto;
import com.moviebookingapp.exception.MovieNotFoundException;
import com.moviebookingapp.exception.TicketAndSeatMisMatch;
import com.moviebookingapp.exception.TicketException;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.repo.MovieRepo;
import com.moviebookingapp.repo.TicketRepo;
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

public class TicketBookingServiceImplTest {

    @Mock
    private TicketRepo ticketRepository;

    @Mock
    private MovieRepo movieTheatreRepo;

    @InjectMocks
    private TicketBookingServiceImpl ticketBookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testBookTicket_InvalidMovieTheatre_ThrowException() {
        // Arrange
        String movieName = "InvalidMovie";
        String theatreName = "InvalidTheatre";
        TicketDto ticketDto = new TicketDto();
        ticketDto.setMovieName(movieName);
        ticketDto.setTheatreName(theatreName);

        when(movieTheatreRepo.findById_MovieNameAndId_TheatreName(movieName, theatreName)).thenReturn(null);

        // Act & Assert
        Assertions.assertThrows(MovieNotFoundException.class, () -> ticketBookingService.bookTicket(ticketDto));

        verify(movieTheatreRepo).findById_MovieNameAndId_TheatreName(movieName, theatreName);
        verifyNoMoreInteractions(ticketRepository);
    }

    @Test
    void testBookTicket_NotEnoughTicketsAvailable_ThrowException() {
        // Arrange
        String movieName = "Movie1";
        String theatreName = "Theatre1";
        int numberOfTickets = 5;
        List<String> seatNumbers = Arrays.asList("A1", "A2");

        TicketDto ticketDto = new TicketDto();
        ticketDto.setMovieName(movieName);
        ticketDto.setTheatreName(theatreName);
        ticketDto.setNumberOfTickets(numberOfTickets);
        ticketDto.setSeatNumbers(seatNumbers);

        Movie movie = new Movie();
        movie.setTotalTicketsAllotted(3);

        when(movieTheatreRepo.findById_MovieNameAndId_TheatreName(movieName, theatreName)).thenReturn(movie);

        // Act & Assert
        Assertions.assertThrows(TicketException.class, () -> ticketBookingService.bookTicket(ticketDto));

        verify(movieTheatreRepo).findById_MovieNameAndId_TheatreName(movieName, theatreName);
        verifyNoMoreInteractions(ticketRepository);
    }

    @Test
    void testBookTicket_TicketAndSeatNumbersMismatch_ThrowException() {
        // Arrange
        String movieName = "Movie1";
        String theatreName = "Theatre1";
        int numberOfTickets = 2;
        List<String> seatNumbers = Arrays.asList("A1", "A2", "A3");

        TicketDto ticketDto = new TicketDto();
        ticketDto.setMovieName(movieName);
        ticketDto.setTheatreName(theatreName);
        ticketDto.setNumberOfTickets(numberOfTickets);
        ticketDto.setSeatNumbers(seatNumbers);

        Movie movie = new Movie();
        movie.setTotalTicketsAllotted(10);

        when(movieTheatreRepo.findById_MovieNameAndId_TheatreName(movieName, theatreName)).thenReturn(movie);

        // Act & Assert
        Assertions.assertThrows(TicketAndSeatMisMatch.class, () -> ticketBookingService.bookTicket(ticketDto));

        verify(movieTheatreRepo).findById_MovieNameAndId_TheatreName(movieName, theatreName);
        verifyNoMoreInteractions(ticketRepository);
    }





    @Test
    void testCancelTicket_TicketDoesNotExist_ReturnZero() {
        // Arrange
        Long ticketId = 1L;

        when(ticketRepository.getById(ticketId)).thenReturn(null);

        // Act
        int result = ticketBookingService.cancelTicket(ticketId);

        // Assert
        Assertions.assertEquals(0, result);

        verify(ticketRepository).getById(ticketId);
        verifyNoMoreInteractions(ticketRepository);
    }
}

