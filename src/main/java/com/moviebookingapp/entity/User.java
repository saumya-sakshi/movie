package com.moviebookingapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id

    @NotBlank
    @Size(min = 3, max = 20)
    private String loginId;

    @NotBlank
    private String firstName;


    @NotBlank
    private String lastName;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Long contactNumber;


    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    @NotBlank
    private String role;
    //to associate two tables
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
    joinColumns = {
            @JoinColumn(name = "USER_ID")
    },
    inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID")
    })
    private Set<Role> roles;

    public User(String loginId, String firstName, String lastName, String email, Long contactNumber, String encode) {
        this.loginId=loginId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.contactNumber=contactNumber;
        this.password=encode;
    }
}
