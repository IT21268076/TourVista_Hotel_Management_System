package com.example.TourVista.DTOs;

import com.example.TourVista.Models.HotelImages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HotelSearchDTO {
    private Long hotelId;
    private String name;
    private String location;
    private List<HotelImages> images;

}
