package com.moviebookingapp.controller;

import com.moviebookingapp.entity.JwtRequest;
import com.moviebookingapp.entity.JwtResponse;
import com.moviebookingapp.entity.User;
import com.moviebookingapp.repository.UserDao;
import com.moviebookingapp.service.JwtService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/v1.0/moviebooking")
public class JwtController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

    @GetMapping("/welcome")
        public String welcome(){
            return "Welcome";

    }

    @PostMapping("/register")
    public User registerNewUser(@RequestBody User user)
    {
        return jwtService.createNewUser(user);
    }
    @PostConstruct
    public  void initRolesAndUser(){
        jwtService.initRolesAndUser();
    }

    @GetMapping("/forAdmin")
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin()
    {
        return "This URL is only accessible to admin";
    }
    @GetMapping("/forUser")
    @PreAuthorize("hasRole('User')")
    public String forUser(){
        return "This URL is only accessible to user";
    }


    // forget password api
    @PutMapping("/{loginId}/forgot")
    public ResponseEntity<String> changePassword(@RequestBody JwtRequest loginRequest, @PathVariable String loginId){
        log.debug("forgot password endopoint accessed by "+loginRequest.getLoginId());
        Optional<User> user1 = userDao.findById(loginId);
        User availableUser = user1.get();
        User updatedUser = new User(
                loginId,
                availableUser.getFirstName(),
                availableUser.getLastName(),
                availableUser.getEmail(),
                availableUser.getContactNumber(),
                passwordEncoder.encode(loginRequest.getPassword())
        );
        updatedUser.setLoginId(availableUser.getLoginId());
        updatedUser.setRoles(availableUser.getRoles());
        userDao.save(updatedUser);
        log.debug(loginRequest.getLoginId()+" has password changed successfully");
        return new ResponseEntity<>("Users password changed successfully", HttpStatus.OK);
    }
}

