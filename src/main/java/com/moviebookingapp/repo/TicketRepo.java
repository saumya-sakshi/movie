package com.moviebookingapp.repo;

import com.moviebookingapp.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {

	List<Ticket> findByMovieNameAndTheatreName(String movie, String theatre);

	

	int countByMovieName(String movieName);
}
