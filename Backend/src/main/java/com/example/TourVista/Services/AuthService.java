package com.example.TourVista.Services;

import com.example.TourVista.DTOs.AuthenticationResponse;
import com.example.TourVista.DTOs.UserDTO;
import com.example.TourVista.Models.User;
import com.example.TourVista.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setMobileNo(request.getMobileNo());
        user.setRole(request.getRole());

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        Long userId = user.getUserId();

        return new AuthenticationResponse(token, userId);

//        UserDTO userDTO = new UserDTO();
//        userDTO.setFirstName(createdUser.getFirstName());
//        userDTO.setLastName(createdUser.getLastName());
//        userDTO.setEmail(createdUser.getEmail());
//        userDTO.setPassword(createdUser.getPassword());
//        userDTO.setMobileNo(createdUser.getMobileNo());
//        userDTO.setRole(createdUser.getRole());
//
//        return userDTO;
    }

    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        Long userId = user.getUserId();

        return new AuthenticationResponse(token, userId);
    }
}
