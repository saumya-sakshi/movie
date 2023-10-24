package com.moviebookingapp.service;

import com.moviebookingapp.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminMovieService {
	
	public List<Ticket> getAllBookedDetails();
	
	public int getBookedTicketsCountByMovieName(String movieName);
	
	public String updateTicketAvailability(String movieName);

	public String deleteMovieById(String movieName, String theatreName);

}
