package com.example.TourVista.DTOs;

import com.example.TourVista.Models.SaveDiscount;
import com.example.TourVista.Models.SaveRoomType;
import com.example.TourVista.Models.SaveSupplements;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyBookingDTO {
    private Long bookingId;
    private double totalAmount;
    private Date checkInDate;
    private Date checkOutDate;
    private LocalDate dateOfBooking;
    private String roomType;
    private double roomPrice;
    private int roomCount;
    private int noOfGuests;
    private Set<String> supplementNames;
    private List<Double> supplementPrices;
    private Set<String> discountNames;
    private List<Double> discountAmounts;
}
