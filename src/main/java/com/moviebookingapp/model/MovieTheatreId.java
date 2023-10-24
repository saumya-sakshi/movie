package com.moviebookingapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class MovieTheatreId implements Serializable {



    @Column(name = "movieName")
    private String movieName;

    @Column(name = "theatreName")
    private String theatreName;
    
    public MovieTheatreId() {
    }

    public MovieTheatreId(String movieName, String theatreName) {
        this.movieName = movieName;
        this.theatreName = theatreName;
    }

}
