package com.moviebookingapp.repo;


import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.MovieTheatreId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<Movie, MovieTheatreId> {
   // @Query("SELECT mt FROM MovieTheatre mt WHERE mt.id.movieName = :movieName")
	Movie findById_MovieName(String movieName);


	Movie findById_TheatreName(String theatreName);

	Movie findById_MovieNameAndId_TheatreName(String movieName, String theatreName);
}