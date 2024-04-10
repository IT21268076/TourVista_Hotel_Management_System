package com.example.TourVista.Repositories;

import com.example.TourVista.Models.SaveRoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveRoomTypeRepository extends JpaRepository<SaveRoomType, Long> {
    SaveRoomType findSaveRoomTypeByBooking_BookingId(Long bookingId);

    SaveRoomType findByBooking_BookingId(Long bookingId);

}
