package com.example.TourVista.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "hotelId")
public class Hotel {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long hotelId;
    private String name;
    private String no;
    private String street;
    private String city;
    @Column(columnDefinition="TEXT")
    private String description;
    private String email;
    private String contactNo;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private Set<Contract> contracts;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private Set<HotelImages> hotelImages;

    public Hotel(Long hotelId, String oldName, String no, String street, String city, String email, String contactNo) {
    }
}
