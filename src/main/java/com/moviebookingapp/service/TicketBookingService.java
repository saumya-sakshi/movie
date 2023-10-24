package com.moviebookingapp.service;

import com.moviebookingapp.dto.TicketDto;
import com.moviebookingapp.model.Ticket;
import org.springframework.stereotype.Service;


@Service
public interface TicketBookingService {
	
//	public Ticket bookTicket(MovieTheatre movieTheatre, int numberOfTickets, List<String> seatNumbers);

	public Ticket bookTicket(TicketDto ticket);

	public int cancelTicket(Long id);
}
