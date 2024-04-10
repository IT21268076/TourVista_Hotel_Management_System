package com.example.TourVista.Repositories;

import com.example.TourVista.Models.SaveDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaveDiscountsRepository extends JpaRepository<SaveDiscount, Long> {
    List<SaveDiscount> findByBooking_BookingId(Long bookingId);
}
