package com.moviebookingapp.serviceImpl;

import com.moviebookingapp.dto.MovieReqDto;
import com.moviebookingapp.dto.MovieResDto;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.repo.MovieRepo;
import com.moviebookingapp.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserMovieServiceImpl implements UserMovieService {
	
	@Autowired
	private MovieRepo movieRepo;
	

	@Override
	public List<Movie> getAllMovies() {
		
		List<Movie> findAllMovies = movieRepo.findAll();
		
		return findAllMovies;
	}

	@Override
	public Movie MovieByName(String name) {
		
		Movie byName = movieRepo.findById_MovieName(name);
		return byName;
	}

	@Override
	public Movie save(Movie movie) {
		Movie movieDetails = movieRepo.save(movie);
		return movieDetails;
	}
	


	@Override
	public MovieResDto addMovieTheatre(MovieReqDto movieTheatre) {
		
		Movie movie = new Movie();
		
		movie.setId(movieTheatre.getId());
		movie.setTotalTicketsAllotted(movieTheatre.getTotalTicketsAllotted());
		movie.setTicketStatus(movieTheatre.getTicketStatus());
		
		Movie save = movieRepo.save(movie);
		
		MovieResDto movieRes = new MovieResDto();
		
		movieRes.setMovieName(save.getId().getMovieName());
		movieRes.setTheatreName(save.getId().getTheatreName());
		movieRes.setTicketStatus(save.getTicketStatus());
		
		return movieRes;
	}

	@Override
	@Transactional
	public String deleteMovieById(MovieReqDto movie) {
		
		Movie name = movieRepo.findById_MovieNameAndId_TheatreName(movie.getId().getMovieName(), movie.getId().getTheatreName());
		if(name != null)
		{
			movieRepo.deleteById(name.getId());
			return name.getId().getMovieName();
		}
		else
		{
		return null;
		}
	}



}
