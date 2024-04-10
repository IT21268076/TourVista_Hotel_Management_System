package com.example.TourVista.Repositories;

import com.example.TourVista.Models.Booking;
import com.example.TourVista.Models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomType(RoomType roomType);

    List<Booking> findBookingsByUser_UserId(Long userId);

    List<Booking> findByCheckOutDateBefore(Date checkOutDate);

    @Query("SELECT b FROM Booking b WHERE b.roomType = :roomType " +
            "AND b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate")
    List<Booking> findByRoomTypeAndDates(@Param("roomType") RoomType roomType,
                                         @Param("checkInDate") Date checkInDate,
                                         @Param("checkOutDate") Date checkOutDate);

    Booking findByBookingId(Long bookingId);

}
