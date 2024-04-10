package com.example.TourVista.Repositories;

import com.example.TourVista.Models.SaveSupplements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaveSupplementsRepository extends JpaRepository<SaveSupplements, Long> {
    List<SaveSupplements> findByBooking_BookingId(Long bookingId);
}
