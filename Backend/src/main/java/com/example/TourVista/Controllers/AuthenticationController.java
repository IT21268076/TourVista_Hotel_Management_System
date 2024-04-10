package com.example.TourVista.Controllers;

import com.example.TourVista.DTOs.AuthenticationResponse;
import com.example.TourVista.DTOs.UserDTO;
import com.example.TourVista.Models.User;
import com.example.TourVista.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request){
//        UserDTO createdUser = authService.createUser(signupRequest);
//        if(createdUser == null)
//            return new ResponseEntity<>("User is not created. Try again later!!", HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ){
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
