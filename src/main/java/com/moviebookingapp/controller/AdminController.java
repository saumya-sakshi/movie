package com.moviebookingapp.controller;

import com.moviebookingapp.dto.MovieReqDto;
import com.moviebookingapp.dto.MovieResDto;
import com.moviebookingapp.exception.MovieNotFoundException;
import com.moviebookingapp.exception.TicketNotCreatedException;
import com.moviebookingapp.exception.TicketStatusException;
import com.moviebookingapp.model.Movie;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.repo.MovieRepo;
import com.moviebookingapp.repo.TicketRepo;
import com.moviebookingapp.service.AdminMovieService;
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
@RequestMapping("/api/v1.0/moviebooking")
@CrossOrigin
public class AdminController {

	@Autowired
	private AdminMovieService adminService;

	@Autowired
	private UserMovieService movieTheatreService;
	@Autowired
	private TicketRepo ticketRepo;
	@Autowired
	private MovieRepo movieRepo;

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@PostMapping("/add-movie-theatres")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<MovieResDto> addMovieTheatre(@RequestBody MovieReqDto movieTheatre) {
		logger.debug("Received addMovieTheatre request");
		MovieResDto movieTheatreDetails;
		try {
			movieTheatreDetails = movieTheatreService.addMovieTheatre(movieTheatre);
			logger.info("Movie theatre added successfully");
			return new ResponseEntity<>(movieTheatreDetails, HttpStatus.CREATED);
		} catch (TicketNotCreatedException e) {
			logger.error("Error occurred while adding movie theatre: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/all")
	public ResponseEntity<List<Movie>> getAllMoviesAdm() {
		logger.debug("Received getAllMoviesAdm request");
		List<Movie> allMovies = movieTheatreService.getAllMovies();
		return new ResponseEntity<>(allMovies, HttpStatus.OK);
	}

	@DeleteMapping(path = "/{movieName}/delete/{theatreName}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<String> deleteMovieById(@PathVariable String movieName, @PathVariable String theatreName) {
		logger.debug("Received deleteMovieById request");
		String movieById = adminService.deleteMovieById(movieName, theatreName);
		logger.info("Deleted movie by id: {}", movieById);
		if (movieById == null) {
			throw new MovieNotFoundException("The movie with name " + movieName + " is not present !");
		}

		return new ResponseEntity<>("The movie with name " + movieName + " is deleted successfully !", HttpStatus.OK);
	}

	@GetMapping(path = "/ticketCount")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<?> getALLBookedTicketsByMovieName(@RequestParam String movieName) {
		logger.debug("Received getALLBookedTicketsByMovieName request");
		int bookedTicketsCount = adminService.getBookedTicketsCountByMovieName(movieName);
		return ResponseEntity.ok(bookedTicketsCount);
	}

	@PutMapping(path = "/ticketStatus")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<String> updateTicketAvailability(@RequestParam String movieName) {
		logger.debug("Received updateTicketAvailability request");
		String ticketAvailability = adminService.updateTicketAvailability(movieName);
		if (ticketAvailability != null) {
			logger.info("Ticket availability updated successfully");
			return ResponseEntity.ok("Ticket availability updated successfully");
		} else {
			throw new TicketStatusException("Ticket Booking Unsuccessful");
		}
	}

	@GetMapping(path="/getAllTickets")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<List<Ticket>> getAllBookedTickets() throws Exception {
		logger.debug("Admin has entered to get status of all booked tickets");
		List<Ticket> allBookedTickets =ticketRepo.findAll();
		if(allBookedTickets==null){
			throw new Exception("Yet no ticket has been booked!");
		}
		return ResponseEntity.ok(allBookedTickets);
	}

	@GetMapping(path="/ticketcount")
	public ResponseEntity<String> getTicketCount(@RequestParam String movieName) throws Exception {
		Movie movie = movieRepo.findById_MovieName(movieName);
		if(movie==null){
			throw new Exception("No Movie found");
		}
		int count = movie.getTotalTicketsAllotted();
		String status=movie.getTicketStatus();
		if (count==0){
			movie.setTicketStatus("SOLD_OUT");
		}
		return ResponseEntity.ok(movieName+" is "+status+" and has "+count+" tickets left!");

	}
	 @PutMapping(path = "/{movieName}/update/")
	 @PreAuthorize("hasRole('Admin')")
	 public ResponseEntity<String> updateStatus(@PathVariable String movieName) throws Exception {
		 Movie movie = movieRepo.findById_MovieName(movieName);
		 if(movie==null){
			 throw new Exception("No Movie found");
		 }
		 int count = movie.getTotalTicketsAllotted();
		 if (count==0){
			 movie.setTicketStatus("SOLD_OUT");
			 return ResponseEntity.ok("Tickets status updated! It has been Sold out");
		 }
		 return ResponseEntity.ok(movieName+" has "+count+" tickets left! BOOK ASAP");

	 }
}
