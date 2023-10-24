package com.moviebookingapp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Movie name is required")
    private String movieName;

    @NotBlank(message = "Theatre name is required")
    private String theatreName;

    @Min(value = 1, message = "Number of tickets must be at least 1")
    private int numberOfTickets;

    @NotEmpty(message = "At least one seat number must be provided")
    @ElementCollection
    private List<@NotBlank(message = "Seat number is required") String> seatNumbers;

}


