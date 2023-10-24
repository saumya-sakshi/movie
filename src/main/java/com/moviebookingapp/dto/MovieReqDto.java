package com.moviebookingapp.dto;


import com.moviebookingapp.model.MovieTheatreId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieReqDto {
	
	@EmbeddedId
    private MovieTheatreId id;
	private int totalTicketsAllotted;
	private String ticketStatus;

}
