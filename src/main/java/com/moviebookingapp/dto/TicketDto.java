package com.moviebookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
	
    private String movieName;

    private String theatreName;

    private int numberOfTickets;

    private List<String> seatNumbers;

}
