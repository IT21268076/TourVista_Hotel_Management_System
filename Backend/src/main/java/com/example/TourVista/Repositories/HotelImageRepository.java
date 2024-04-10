package com.example.TourVista.Repositories;

import com.example.TourVista.Models.HotelImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface HotelImageRepository extends JpaRepository<HotelImages, Long> {
    Set<HotelImages> findByHotel_HotelId(Long hotelId);
//    Set<HotelImages> findByHotelId();
}
