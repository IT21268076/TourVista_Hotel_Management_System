package com.example.TourVista.DTOs;

import com.example.TourVista.Utils.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String token;
    private Long userId;

//    private String firstName;
//    private String lastName;
//    private String email;
//    private String password;
//    private String mobileNo;
//    private UserRole role;

    public AuthenticationResponse(String token, Long userId) {

        this.token = token;
        this.userId = userId;
    }
}
