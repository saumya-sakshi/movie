package com.moviebookingapp.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {
	
	@EmbeddedId
    private MovieTheatreId id;



    @Min(value = 0, message = "Total tickets allotted must be a positive number or zero")
    private int totalTicketsAllotted;

    public Movie(MovieTheatreId id, int tickets) {
        this.id = id;
        this.totalTicketsAllotted=tickets;
    }

//    @OneToMany(mappedBy = "movieTheatre")
//    @Transient
//    private Set<Ticket> tickets;
    
    public MovieTheatreId getId() {
        return id;
    }
    
    public String ticketStatus;
    
    public Movie(String movieName, String theatreName, int totalTicketsAllotted) {
        this.id = new MovieTheatreId(movieName, theatreName);
        this.totalTicketsAllotted = totalTicketsAllotted;
    }

    public void setId(MovieTheatreId id) {
        this.id = id;
    }

	}

