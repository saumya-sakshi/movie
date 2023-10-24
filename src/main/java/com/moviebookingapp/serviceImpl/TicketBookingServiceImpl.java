package com.moviebookingapp.serviceImpl;


import com.moviebookingapp.dto.TicketDto;
import com.moviebookingapp.exception.MovieNotFoundException;
import com.moviebookingapp.exception.SeatAlreadyBookedException;
import com.moviebookingapp.exception.TicketAndSeatMisMatch;
import com.moviebookingapp.exception.TicketException;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.repo.MovieRepo;
import com.moviebookingapp.repo.TicketRepo;
import com.moviebookingapp.service.TicketBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;



@Service
public class TicketBookingServiceImpl implements TicketBookingService {

    @Autowired
    private TicketRepo ticketRepository;
    
    @Autowired
    private MovieRepo movieTheatreRepo;




	@Override
	public Ticket bookTicket(TicketDto ticket) {
		Movie movie = movieTheatreRepo.findById_MovieNameAndId_TheatreName(ticket.getMovieName(), ticket.getTheatreName());

		if (movie == null) {
			throw new MovieNotFoundException("Invalid movie or theatre");
		}

		if (ticket.getNumberOfTickets() > movie.getTotalTicketsAllotted()) {
			throw new TicketException("Not enough tickets available");
		}

		if (ticket.getNumberOfTickets() != ticket.getSeatNumbers().size()) {
			throw new TicketAndSeatMisMatch("Number of tickets and number of seats do not match.");
		}

		List<String> seatNumbers = ticket.getSeatNumbers();

		List<Ticket> existingTickets = ticketRepository.findByMovieNameAndTheatreName(movie.getId().getMovieName(), movie.getId().getTheatreName());
		if (existingTickets != null) {
			for (Ticket existingTicket : existingTickets) {
				if (existingTicket.getSeatNumbers() != null) {
					for (String bookedSeat : existingTicket.getSeatNumbers()) {
						if (seatNumbers.contains(bookedSeat)) {
							throw new SeatAlreadyBookedException("One or more seats are already booked.");
						}
					}
				}
			}
		}

		movie.setTotalTicketsAllotted(movie.getTotalTicketsAllotted() - ticket.getNumberOfTickets());
		movieTheatreRepo.save(movie);

		Ticket tickets = new Ticket();
		tickets.setMovieName(ticket.getMovieName());
		tickets.setTheatreName(ticket.getTheatreName());
		tickets.setNumberOfTickets(ticket.getNumberOfTickets());
		tickets.setSeatNumbers(ticket.getSeatNumbers());
		Ticket save = ticketRepository.save(tickets);
return  save;
//		TicketDto ticketDto = new TicketDto();
//		ticketDto.setMovieName(save.getMovieName());
//		ticketDto.setTheatreName(save.getTheatreName());
//		ticketDto.setNumberOfTickets(save.getNumberOfTickets());
//		ticketDto.setSeatNumbers(save.getSeatNumbers());

		//return ticketDto;
	}


	@Override
	@Transactional
	public int cancelTicket(Long id) {
		
		Ticket getById = ticketRepository.getById(id);
		if(getById == null)
		{
			return 0;
		}
		else
		{
			ticketRepository.deleteById(getById.getId());
			return 1;
		}
	}
}
