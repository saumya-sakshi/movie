package com.moviebookingapp.test;

import com.moviebookingapp.dto.MovieReqDto;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.MovieTheatreId;
import com.moviebookingapp.repo.MovieRepo;
import com.moviebookingapp.serviceImpl.UserMovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserMovieServiceImplTest {

    @Mock
    private MovieRepo movieRepo;

    @InjectMocks
    private UserMovieServiceImpl userMovieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie());
        movies.add(new Movie());

        when(movieRepo.findAll()).thenReturn(movies);

        List<Movie> result = userMovieService.getAllMovies();

        assertEquals(2, result.size());
        verify(movieRepo, times(1)).findAll();
    }

    @Test
    public void testMovieByName() {
        String movieName = "Movie 1";
        Movie movie = new Movie();
        movie.setId(new MovieTheatreId(movieName, "Theatre 1"));

        when(movieRepo.findById_MovieName(movieName)).thenReturn(movie);

        Movie result = userMovieService.MovieByName(movieName);

        assertEquals(movieName, result.getId().getMovieName());
        verify(movieRepo, times(1)).findById_MovieName(movieName);
    }

    @Test
    public void testSave() {
        Movie movie = new Movie();
        movie.setId(new MovieTheatreId("Movie 1", "Theatre 1"));
        movie.setTotalTicketsAllotted(100);

        when(movieRepo.save(movie)).thenReturn(movie);

        Movie result = userMovieService.save(movie);

        assertEquals("Movie 1", result.getId().getMovieName());
        verify(movieRepo, times(1)).save(movie);
    }



    @Test
    public void testDeleteMovieById() {
        String movieName = "Movie 1";
        String theatreName = "Theatre 1";

        MovieReqDto movieReqDto = new MovieReqDto();
        movieReqDto.setId(new MovieTheatreId(movieName, theatreName));

        Movie movie = new Movie();
        movie.setId(movieReqDto.getId());

        when(movieRepo.findById_MovieNameAndId_TheatreName(movieName, theatreName)).thenReturn(movie);
        doNothing().when(movieRepo).deleteById(movie.getId());

        String result = userMovieService.deleteMovieById(movieReqDto);

        assertEquals(movieName, result);
        verify(movieRepo, times(1)).findById_MovieNameAndId_TheatreName(movieName, theatreName);
        verify(movieRepo, times(1)).deleteById(movie.getId());
    }
}

