package com.moviebookingapp.serviceImpl;

import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.repo.MovieRepo;
import com.moviebookingapp.repo.TicketRepo;
import com.moviebookingapp.service.AdminMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminMovieServiceImpl implements AdminMovieService {
	
	@Autowired
	private TicketRepo ticketRepo;
	
	@Autowired
	private MovieRepo movieRepo;

	@Override
	public List<Ticket> getAllBookedDetails() {
		
		List<Ticket> allBookedDetails = ticketRepo.findAll();
		return allBookedDetails;
	}
	
	@Override
	public int getBookedTicketsCountByMovieName(String movieName) {
		System.out.println("count from service"+ticketRepo.countByMovieName(movieName));
        int count = ticketRepo.countByMovieName(movieName);
        return count;
    }

	@Override
	public String updateTicketAvailability(String movieName) {

		 Movie movie = movieRepo.findById_MovieName(movieName);


		 if(movie != null)
		 {

	        int totalTicketsBooked = ticketRepo.countByMovieName(movieName);
	        int remainingTickets = movie.getTotalTicketsAllotted() - totalTicketsBooked;

	        if (remainingTickets < 1) {
	            movie.setTicketStatus("Sold out");
	        } else if(remainingTickets<=20) {
	            movie.setTicketStatus("BOOK ASAP");
	        }
			else {
				movie.setTicketStatus("Available");
			}
	        movieRepo.save(movie);
		return "Status updated successfully !!!";
	     }
		 else
		 {
			 return null;
		 }

}
	@Override
	public String deleteMovieById(String movieName, String theatreName) {
		Movie name = movieRepo.findById_MovieNameAndId_TheatreName(movieName,theatreName);
		if (name != null) {
			movieRepo.deleteById(name.getId());
			return "record deleted successfully";
		} else {
			return null;
		}
	}

}
