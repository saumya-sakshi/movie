package com.moviebookingapp.controller;

import com.moviebookingapp.dto.TicketDto;
import com.moviebookingapp.exception.MovieNotFoundException;
import com.moviebookingapp.exception.TicketNotCreatedException;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.service.TicketBookingService;
import com.moviebookingapp.service.UserMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1.0/moviebooking")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private TicketBookingService ticketBookingService;

	@Autowired
	private UserMovieService userMovieService;



	@GetMapping(path = "search/{name}")
	public ResponseEntity<Movie> getMovieByName(@PathVariable String name) {
		logger.debug("Received search movie request");
		Movie byName = userMovieService.MovieByName(name);
		if (byName == null) {
			logger.error("Movie not found with name: {}", name);
			throw new MovieNotFoundException("Movie not found with name: " + name);
		} else {
			logger.info("Retrieved movie by name: {}", name);
			return new ResponseEntity<>(byName, HttpStatus.OK);
		}
	}

	@PostMapping(path = "{moviename}/add")
	@PreAuthorize("hasRole('User')")
	public ResponseEntity<Ticket> bookTicket(@RequestBody TicketDto ticket) {
		logger.debug("Received bookTicket request");
		Ticket bookTicket = ticketBookingService.bookTicket(ticket);
		if (bookTicket == null) {
			logger.error("Ticket booking unsuccessful");
			throw new TicketNotCreatedException("Ticket Booking Unsuccessful");
		}
		logger.info("Ticket booked successfully");
		return new ResponseEntity<>(bookTicket, HttpStatus.CREATED);
	}

	@DeleteMapping(path = "/cancelTicket")
	@PreAuthorize("hasRole('User')")
	public ResponseEntity<String> cancelTicket(@RequestParam Long id) {
		logger.debug("Received cancelTicket request");
		int cancelTicket = ticketBookingService.cancelTicket(id);
		if (cancelTicket == 0) {
			logger.error("Ticket with id {} is not booked/created", id);
			throw new TicketNotCreatedException("Ticket with id " + id + " is not booked/created");
		} else {
			logger.info("Cancelled ticket with id: {}", cancelTicket);
			return new ResponseEntity<>("The ticket with id " + cancelTicket + " is deleted successfully!", HttpStatus.OK);
		}
	}
}
