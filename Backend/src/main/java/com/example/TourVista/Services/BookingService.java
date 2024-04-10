package com.example.TourVista.Services;

import com.example.TourVista.DTOs.BookingDTO;
import com.example.TourVista.DTOs.DiscountDTO;
import com.example.TourVista.DTOs.MyBookingDTO;
import com.example.TourVista.Models.*;
import com.example.TourVista.Repositories.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomSeasonPriceService roomSeasonPriceService;
    @Autowired
    private RoomSeasonPriceRepository roomSeasonPriceRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SaveRoomTypeRepository saveRoomTypeRepository;
    @Autowired
    private SaveSupplementsRepository saveSupplementsRepository;
    @Autowired
    private SaveDiscountsRepository saveDiscountsRepository;
    @Autowired
    private RoomTypeService roomTypeService;

    //public BookingService(){}

    public BookingService(
            DiscountRepository discountRepository,
            ContractRepository contractRepository,
            BookingRepository bookingRepository,
            RoomSeasonPriceRepository roomSeasonPriceRepository,
            RoomSeasonPriceService roomSeasonPriceService,
            RoomTypeRepository roomTypeRepository,
            UserRepository userRepository,
            SaveRoomTypeRepository saveRoomTypeRepository,
            SaveDiscountsRepository saveDiscountsRepository,
            SaveSupplementsRepository saveSupplementsRepository,
            RoomTypeService roomTypeService
    ) {
        this.bookingRepository = bookingRepository;
        this.roomSeasonPriceRepository = roomSeasonPriceRepository;
        this.roomSeasonPriceService = roomSeasonPriceService;
        this.roomTypeRepository = roomTypeRepository;
        this.discountRepository = discountRepository;
        this.contractRepository = contractRepository;
        this.userRepository= userRepository;
        this.saveRoomTypeRepository = saveRoomTypeRepository;
        this.saveSupplementsRepository = saveSupplementsRepository;
        this.saveDiscountsRepository = saveDiscountsRepository;
        this.roomTypeService = roomTypeService;
    }

    public BookingService() {

    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

//    @Transactional
//    public void addNewBooking(Long roomTypeId, Long seasonId,BookingDTO bookingDTO) {
//        Booking booking = new Booking();
//        BeanUtils.copyProperties(bookingDTO, booking);
//
//        // Fetch RoomType based on roomTypeId
//        RoomType roomType = roomTypeRepository.findById(roomTypeId)
//                .orElseThrow(() -> new IllegalArgumentException("RoomType not found for id: " + roomTypeId));
//
//        booking.setRoomType(roomType);
//
//        // Retrieve the Contract ID associated with the RoomType
//        Long contractId = getContractIdByRoomTypeId(roomTypeId);
//
//        // Retrieve discounts by contractId
//        Set<Discount> discounts = discountRepository.findDiscountsByContract_ContractId(contractId);
//        Set<SaveDiscount> saveDiscountSet = new HashSet<>();
//        // Create a save discount object Set
//        // Save Discounts
//        for (Discount discount : discounts) {
//            SaveDiscount saveDiscount = new SaveDiscount();
//            saveDiscount.setAmount(discount.getAmount());
//            saveDiscountSet.add(saveDiscount);
//        }
//
//        booking.setSaveDiscounts(saveDiscountSet);
//
//        //Set<SaveSupplements> saveSupplementsSet = bookingDTO.getSaveSupplements();
//        Set<SaveSupplements> saveSupplementsSet = new HashSet<>();
//        // Add supplements from bookingDTO to saveSupplementsSet
//        for (SaveSupplements saveSupplements : bookingDTO.getSaveSupplements()) {
//            SaveSupplements supplement = new SaveSupplements();
//            supplement.setName(saveSupplements.getName());
//            supplement.setPrice(saveSupplements.getPrice());
//            saveSupplementsSet.add(supplement);
//        }
////        for (SaveSupplements saveSupplements : saveSupplementsSet) {
////
////            saveSupplements.setName(saveSupplements.getName());
////            saveSupplements.setPrice(saveSupplements.getPrice());
////
////            saveSupplementsSet.add(saveSupplements);
////        }
//
//        booking.setSaveSupplements(saveSupplementsSet);
//
//        SaveRoomType saveRoomType = new SaveRoomType();
//        saveRoomType.setType(bookingDTO.getRoomType().getType());
//        double price = roomSeasonPriceService.getPriceByRoomTypeAndSeason(roomTypeId,seasonId);
//        saveRoomType.setPrice(price);
//
//        booking.setSaveRoomType(saveRoomType);
//
//        bookingRepository.save(booking);
//    }

    @Transactional
    public void addNewBooking(Long roomTypeId, Long seasonId, BookingDTO bookingDTO) {
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDTO, booking);

        // Fetch RoomType based on roomTypeId
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("RoomType not found for id: " + roomTypeId));

        boolean isAvailable = roomTypeService.isRoomAvailable(roomType, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate(), bookingDTO.getNumberOfRooms());

        if(isAvailable){
            booking.setRoomType(roomType);

            // Retrieve the Contract ID associated with the RoomType
            Long contractId = getContractIdByRoomTypeId(roomTypeId);

            // Retrieve discounts by contractId
            Set<SaveDiscount> saveDiscountSet = bookingDTO.getSaveDiscounts().stream()
                    .map(discount -> {
                        SaveDiscount saveDiscount = new SaveDiscount();
                        saveDiscount.setAmount(discount.getAmount());
                        saveDiscount.setStartDate(discount.getStartDate());
                        saveDiscount.setEndDate(discount.getEndDate());
                        saveDiscount.setName(discount.getName());
                        saveDiscount.setBooking(booking);
                        return saveDiscount;
                    })
                    .collect(Collectors.toSet());

            saveDiscountsRepository.saveAll(saveDiscountSet);
            //booking.setSaveDiscounts(saveDiscountSet);

            // Initialize saveSupplementsSet properly
            Set<SaveSupplements> saveSupplementsSet = bookingDTO.getSelectedSupplements().stream()
                    .map(saveSupplements -> {
                        SaveSupplements supplement = new SaveSupplements();
                        supplement.setName(saveSupplements.getName());
                        supplement.setPrice(saveSupplements.getPrice());
                        supplement.setBooking(booking);
                        return supplement;
                    })
                    .collect(Collectors.toSet());

            saveSupplementsRepository.saveAll(saveSupplementsSet);

            //booking.setSaveSupplements(saveSupplementsSet);

            SaveRoomType saveRoomType = new SaveRoomType();
            saveRoomType.setType(bookingDTO.getType());
            saveRoomType.setPrice(bookingDTO.getRoomTypePrice());
            saveRoomType.setRoomCount(bookingDTO.getNumberOfRooms());
            saveRoomType.setNoOfGuests(bookingDTO.getNoOfGuests());

            saveRoomType.setBooking(booking);

            saveRoomTypeRepository.save(saveRoomType);

            booking.setDateOfBooking(LocalDate.now());
            booking.setNumberOfRooms(bookingDTO.getNumberOfRooms());

            Long userId = bookingDTO.getUserId();

            User user = userRepository.getUserByUserId(userId);
            booking.setUser(user);

            bookingRepository.save(booking);

            // Call the method to update room count for expired bookings
            updateRoomCountForExpiredBookings(seasonId);
        }else{
            throw new IllegalArgumentException("Room is not available for the specified dates and number of rooms.");
        }

    }

    public void updateRoomCountForExpiredBookings(Long seasonId) {
        Date currentDate = new Date();
        List<Booking> expiredBookings = bookingRepository.findByCheckOutDateBefore(currentDate);

        for (Booking booking : expiredBookings) {
            RoomType roomType = booking.getRoomType();
            RoomSeasonPrice roomSeasonPrice = roomSeasonPriceRepository.findByRoomTypeAndSeason_SeasonId(roomType, seasonId);
            if (roomSeasonPrice != null) {
                int initialRoomCount = roomSeasonPrice.getInitialRoomCount();
                int currentRoomCount = roomSeasonPrice.getRoomCount();
                if (currentRoomCount < initialRoomCount) {
                    roomSeasonPrice.setRoomCount(currentRoomCount + 1);
                    roomSeasonPriceRepository.save(roomSeasonPrice);
                }
            }
        }
    }

//    public Long getContractIdByRoomTypeId(Long roomTypeId) {
//        RoomType roomType = roomTypeRepository.findById(roomTypeId)
//                .orElseThrow(() -> new IllegalArgumentException("RoomType not found for id: " + roomTypeId));
//
//        // Access the associated Contract and retrieve its contractId
//        Contract contract = roomType.getContractId();
//        if (contract != null) {
//            return contract.getContractId();
//        } else {
//            // Handle the case where no contract is associated with the RoomType
//            return null; // Or throw an exception, depending on your requirements
//        }
//
//    }
    public Long getContractIdByRoomTypeId(Long roomTypeId) {
        // Retrieve contracts associated with the given roomTypeId
        List<Contract> contracts = contractRepository.findByRoomTypes_RoomTypeId(roomTypeId);

        // If contracts exist, return the ID of the first contract
        if (!contracts.isEmpty()) {
            return contracts.get(0).getContractId();
        } else {
            // Handle the case where no contracts are associated with the RoomType
            throw new IllegalArgumentException("No contract found for RoomType ID: " + roomTypeId);
        }
    }

    @Transactional
    public void updateBooking(Long bookingId, Booking booking) {
        Booking bookingExists = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalStateException(("Booking with id: "+ bookingId + " does not exists")));
        bookingExists.setTotalAmount(booking.getTotalAmount());
        bookingExists.setCheckInDate(booking.getCheckInDate());
        bookingExists.setCheckOutDate(booking.getCheckOutDate());
        bookingExists.setStatus(booking.getStatus());

        bookingRepository.save(bookingExists);
    }

    public List<MyBookingDTO> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findBookingsByUser_UserId(userId);
        Set<Long> processedEntities = new HashSet<>(); // to prevent infinite loop
        List<MyBookingDTO> myBookingDTOs = bookings.stream()
                .map(booking -> convertToMyBookingDTO(booking, processedEntities))
                .collect(Collectors.toList());

        // Print the contents of the list
        myBookingDTOs.forEach(System.out::println);

        return myBookingDTOs;
    }

    private MyBookingDTO convertToMyBookingDTO(Booking booking, Set<Long> processedEntities) {
        MyBookingDTO myBookingDTO = new MyBookingDTO();
        myBookingDTO.setBookingId(booking.getBookingId());
        myBookingDTO.setTotalAmount(booking.getTotalAmount());
        myBookingDTO.setCheckInDate(booking.getCheckInDate());
        myBookingDTO.setCheckOutDate(booking.getCheckOutDate());
        myBookingDTO.setDateOfBooking(booking.getDateOfBooking());
        myBookingDTO.setRoomCount(booking.getNumberOfRooms());

        // Extract data from SaveRoomType
        Long bookingId = booking.getBookingId();
        SaveRoomType saveRoomType = saveRoomTypeRepository.findByBooking_BookingId(bookingId);
        if (saveRoomType != null) {
            myBookingDTO.setRoomType(saveRoomType.getType());
            myBookingDTO.setRoomPrice(saveRoomType.getPrice());
            myBookingDTO.setNoOfGuests(saveRoomType.getNoOfGuests());
        }

        // Extract supplement names
        if (!processedEntities.contains(bookingId)) {
            processedEntities.add(bookingId);
            Set<String> supplementNames = new HashSet<>();
            List<SaveSupplements> supplements = saveSupplementsRepository.findByBooking_BookingId(bookingId);
            for (SaveSupplements supplement : supplements) {
                supplementNames.add(supplement.getName());
            }
            myBookingDTO.setSupplementNames(supplementNames);
        }

        // Calculate total discount amount from SaveDiscounts
//        double discountAmount = saveDiscountRepository.calculateTotalDiscountByBookingId(bookingId);
//        myBookingDTO.setDiscountAmount(discountAmount);

        return getMyBookingDTO(bookingId, myBookingDTO);
    }

    public MyBookingDTO getBookingByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);

        MyBookingDTO myBookingDTO = new MyBookingDTO();
        myBookingDTO.setBookingId(booking.getBookingId());
        myBookingDTO.setTotalAmount(booking.getTotalAmount());
        myBookingDTO.setCheckInDate(booking.getCheckInDate());
        myBookingDTO.setCheckOutDate(booking.getCheckOutDate());
        myBookingDTO.setDateOfBooking(booking.getDateOfBooking());
        myBookingDTO.setRoomCount(booking.getNumberOfRooms());

        // Extract data from SaveRoomType

        SaveRoomType saveRoomType = saveRoomTypeRepository.findByBooking_BookingId(bookingId);
        if (saveRoomType != null) {
            myBookingDTO.setRoomType(saveRoomType.getType());
            myBookingDTO.setRoomPrice(saveRoomType.getPrice());
            myBookingDTO.setNoOfGuests(saveRoomType.getNoOfGuests());
        }

        // Extract supplement names
        Set<String> supplementNames = new HashSet<>();
        List<Double> supplementPrices = new ArrayList<>();
        List<SaveSupplements> supplements = saveSupplementsRepository.findByBooking_BookingId(bookingId);
        for (SaveSupplements supplement : supplements) {
            supplementNames.add(supplement.getName());
            supplementPrices.add(supplement.getPrice());
        }
        myBookingDTO.setSupplementNames(supplementNames);
        myBookingDTO.setSupplementPrices(supplementPrices);


        // Calculate total discount amount from SaveDiscounts
//        double discountAmount = saveDiscountRepository.calculateTotalDiscountByBookingId(bookingId);
//        myBookingDTO.setDiscountAmount(discountAmount);

        return getMyBookingDTO(bookingId, myBookingDTO);
    }

    private MyBookingDTO getMyBookingDTO(Long bookingId, MyBookingDTO myBookingDTO) {
        List<Double> discountAmounts = new ArrayList<>();
        Set<String> discountNames = new HashSet<>();
        List<SaveDiscount> saveDiscounts = saveDiscountsRepository.findByBooking_BookingId(bookingId);
        for (SaveDiscount saveDiscount : saveDiscounts) {
            discountAmounts.add(saveDiscount.getAmount());
            discountNames.add(saveDiscount.getName());
        }
        myBookingDTO.setDiscountAmounts(discountAmounts);
        myBookingDTO.setDiscountNames(discountNames);

        return myBookingDTO;
    }
}
