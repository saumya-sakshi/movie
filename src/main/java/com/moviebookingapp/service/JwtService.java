package com.moviebookingapp.service;

import com.moviebookingapp.entity.JwtRequest;
import com.moviebookingapp.entity.JwtResponse;
import com.moviebookingapp.entity.Role;
import com.moviebookingapp.entity.User;
import com.moviebookingapp.repository.RoleDao;
import com.moviebookingapp.repository.UserDao;
import com.moviebookingapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
      String userName=  jwtRequest.getLoginId();
      String userPassword=jwtRequest.getPassword();
      authenticate(userName,userPassword);
     final UserDetails userDetails=loadUserByUsername(userName);
     String newToken =jwtUtil.generateToken(userDetails);
    User user= userDao.findById(userName).get();
    return new JwtResponse(user,newToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user=userDao.findById(username).get();
       if(user!=null){
           return new org.springframework.security.core.userdetails.User(
                   user.getLoginId(),
                   user.getPassword(),
                   getAuthorities(user)
           );
       }else{
           throw  new UsernameNotFoundException("username is not valid");
       }
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        }catch(DisabledException e){
            throw new Exception("User is disabled");
        }catch (BadCredentialsException e){
            throw new Exception("Bad credentials from User");
        }
    }

    private Set getAuthorities(User user){
        Set authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });
        return  authorities;
    }



    @Autowired
    private PasswordEncoder passwordEncoder;
    public User createNewUser(User user){
        Role role = roleDao.findById(user.getRole()).get();
        Set<Role> roles= new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(getEncodedPassword(user.getPassword()));
        return userDao.save(user);
    }
    public void initRolesAndUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin Role for Movie Booking System");
        roleDao.save(adminRole);
        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("User Role for Movie Booking System");
        roleDao.save(userRole);
//        User adminUser = new User();
//        adminUser.setLoginId("admin123");
//        adminUser.setFirstName("admin");
//        adminUser.setLastName("admin");
//        adminUser.setPassword(getEncodedPassword("admin@123"));
//        Set<Role> adminRoles = new HashSet<>();
//        adminUser.setRoles(adminRoles);
//        adminRoles.add(adminRole);
//        userDao.save(adminUser);
//        User user = new User();
//        user.setLoginId("saumya123");
//        user.setFirstName("saumya");
//        user.setLastName("sakshi");
//        user.setPassword(getEncodedPassword("saumya@123"));
//        Set<Role> userRoles = new HashSet<>();
//        userRoles.add(userRole);
//        user.setRoles(userRoles);
//        userDao.save(user);
    }
    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }
}

