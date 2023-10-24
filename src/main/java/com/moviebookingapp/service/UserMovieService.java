package com.moviebookingapp.service;

import com.moviebookingapp.dto.MovieReqDto;
import com.moviebookingapp.dto.MovieResDto;
import com.moviebookingapp.model.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserMovieService {
	
	public List<Movie> getAllMovies();
	
	public Movie MovieByName(String name);

	public Movie save(Movie movie);

	public MovieResDto addMovieTheatre(MovieReqDto movieTheatre);

	public String deleteMovieById(MovieReqDto movie);



}
