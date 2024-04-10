package com.example.TourVista.Services;

import com.example.TourVista.DTOs.HotelDTO;
import com.example.TourVista.DTOs.HotelImageDTO;
import com.example.TourVista.DTOs.HotelSearchDTO;
import com.example.TourVista.Models.Contract;
import com.example.TourVista.Models.Hotel;
import com.example.TourVista.Models.HotelImages;
import com.example.TourVista.Models.SaveRoomType;
import com.example.TourVista.Repositories.ContractRepository;
import com.example.TourVista.Repositories.HotelImageRepository;
import com.example.TourVista.Repositories.HotelRepository;
import com.example.TourVista.Repositories.SaveRoomTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private final HotelRepository hotelRepository;

    @Autowired
    private final ContractRepository contractRepository;

    @Autowired
    private final HotelImageRepository hotelImageRepository;

    @Autowired
    private final SaveRoomTypeRepository saveRoomTypeRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository, ContractRepository contractRepository, HotelImageRepository hotelImageRepository, SaveRoomTypeRepository saveRoomTypeRepository) {
        this.hotelRepository = hotelRepository;
        this.contractRepository = contractRepository;
        this.hotelImageRepository = hotelImageRepository;
        this.saveRoomTypeRepository = saveRoomTypeRepository;
    }

    public List<Hotel> getHotels(){
        return hotelRepository.findAll();
    }



//    public void addNewHotel(HotelDTO hotelDTO) {
//        Hotel hotel = new Hotel();
//        BeanUtils.copyProperties(hotelDTO, hotel);
//
//        Optional<Hotel> hotelOptional = hotelRepository.findHotelByEmail(hotel.getEmail());
//        if(hotelOptional.isPresent()){
//            throw new IllegalStateException("Email already exisits");
//        }
//
//        hotelRepository.save(hotel);
//
//        Set<HotelImageDTO> hotelImageDTOSet = hotelDTO.getHotelImages();
//        Long hotelid = hotel.getHotelId();
//        Set<HotelImages> hotelImageSet = new HashSet<>();
//        for(HotelImageDTO hotelImageDTO : hotelImageDTOSet) {
//            HotelImages hotelImage = new HotelImages();
//
//            hotelImageDTO.setHotelId(hotelid);
//            hotelImage.setImageUrl(hotelImageDTO.getImageUrl());
//            hotelImage.setHotel(hotel);
//
//            hotelImageSet.add(hotelImage);
//
//            hotelImageRepository.save(hotelImage);
//        }
//        hotel.setHotelImages(hotelImageSet);
//
//
//    }

    public void addNewHotel(MultipartFile[] hotelImages, String hotelDataJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HotelDTO hotelDTO = objectMapper.readValue(hotelDataJson, HotelDTO.class);

        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDTO, hotel);

        Optional<Hotel> hotelOptional = hotelRepository.findHotelByEmail(hotel.getEmail());
        if(hotelOptional.isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        hotelRepository.save(hotel);

        // Save hotel images
        if (hotelImages != null) {
            for (MultipartFile image : hotelImages) {
                HotelImages hotelImage = new HotelImages();
                hotelImage.setHotel(hotel);
                hotelImage.setImage(image.getBytes());
                // Save image to storage or database
                hotelImageRepository.save(hotelImage);
            }
        }


    }

    @Transactional
    public void updateHotel(Long hotelId, String name, String no, String street, String city, String email, String contactNo) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new IllegalStateException(("hotel with id: "+ hotelId + " does not exists")));

        if(name != null && !name.isEmpty() && !Objects.equals(hotel.getName(), name)){
            hotel.setName(name);
        }

        if((no != null && !no.isEmpty() && !Objects.equals(hotel.getNo(), no))
            && (street != null && !street.isEmpty() && !Objects.equals(hotel.getStreet(), street))
            && (city != null && !city.isEmpty() && !Objects.equals(hotel.getCity(), city))){
            hotel.setNo(no);
            hotel.setStreet(street);
            hotel.setCity(city);
        }

        if(email != null && !email.isEmpty() && !Objects.equals(hotel.getEmail(), email)){
            Optional<Hotel> hotelOptional = hotelRepository.findHotelByEmail(email);
            if(hotelOptional.isPresent()){
                throw new IllegalStateException("Email taken");
            }
            hotel.setName(email);
        }

        if(contactNo != null && !contactNo.isEmpty() && !Objects.equals(hotel.getContactNo(), contactNo)){
            hotel.setName(contactNo);
        }
    }

    public void deleteHotel(Long hotelId) {
        boolean exists = hotelRepository.existsById(hotelId);
        if(!exists){
            throw new IllegalStateException(("hotel with id : "+hotelId + "don't exists"));
        }
        hotelRepository.deleteById(hotelId);
    }

//    public List<HotelSearchDTO> searchHotels(Date checkInDate, Date checkOutDate, String location) {
//
//        // Step 2: Retrieve Contracts within the Date Range
//        List<Contract> contracts = contractRepository.findByStartDateBeforeAndEndDateAfter(checkInDate, checkOutDate);
//
//        Set<Long> hotelIds = new HashSet<>(); // To avoid duplicate hotels
//
//        // Step 3 & 4: Retrieve Hotel Information and Eliminate Duplicate Hotels
//        for (Contract contract : contracts) {
//            hotelIds.add(contract.getHotel().getHotelId());
//        }
//
//        // Step 5: Filter Hotels by Location
//        List<Hotel> hotelsInLocation = hotelRepository.findByCity(location);
//
//
//
//        return getHotelSearchDTOS(hotelsInLocation, hotelIds);
//    }
//
//    // Step 6: Return the List of Hotels
//    private static List<HotelSearchDTO> getHotelSearchDTOS(List<Hotel> hotelsInLocation, Set<Long> hotelIds) {
//        List<HotelSearchDTO> searchResults = new ArrayList<>();
//        for (Hotel hotel : hotelsInLocation) {
//            if (hotelIds.contains(hotel.getHotelId())) {
//                HotelSearchDTO hotelSearchDTO = new HotelSearchDTO();
//                hotelSearchDTO.setName(hotel.getName());
//                hotelSearchDTO.setHotelId(hotel.getHotelId());
//                //hotelDTO.setImage(hotel.getImage());
//                hotelSearchDTO.setLocation(hotel.getCity());
//                searchResults.add(hotelSearchDTO);
//            }
//        }
//        return searchResults;
//    }

//    public List<HotelSearchDTO> searchHotels(Date checkInDate, Date checkOutDate, String location) {
//        // Step 2: Retrieve Contracts within the Date Range
//        List<Contract> contracts = contractRepository.findByStartDateBeforeAndEndDateAfter(checkInDate, checkOutDate);
//
//        Set<Long> hotelIds = new HashSet<>(); // To avoid duplicate hotels
//
//        // Step 3 & 4: Retrieve Hotel Information and Eliminate Duplicate Hotels
//        for (Contract contract : contracts) {
//            hotelIds.add(contract.getHotel().getHotelId());
//        }
//
//        // Step 5: Retrieve Hotels by IDs
//        List<Hotel> hotelsInLocation = hotelRepository.findAllById(hotelIds);
//
//        // Step 6: Retrieve hotel images for each hotel
//        Map<Long, List<byte[]>> hotelImagesMap = new HashMap<>();
//        for (Long hotelId : hotelIds) {
//            Set<HotelImages> hotelImages = hotelImageRepository.findByHotel_HotelId(hotelId);
//            List<byte[]> images = new ArrayList<>();
//            for (HotelImages image : hotelImages) {
//                images.add(image.getImage());
//            }
//            hotelImagesMap.put(hotelId, images);
//        }
//
//        return getHotelSearchDTOS(hotelsInLocation, hotelImagesMap);
//    }
//
//    private List<HotelSearchDTO> getHotelSearchDTOS(List<Hotel> hotels, Map<Long, List<byte[]>> hotelImagesMap) {
//        List<HotelSearchDTO> hotelSearchDTOs = new ArrayList<>();
//        for (Hotel hotel : hotels) {
//            HotelSearchDTO hotelSearchDTO = new HotelSearchDTO();
//            // Populate hotel details
//            hotelSearchDTO.setHotelId(hotel.getHotelId());
//            hotelSearchDTO.setName(hotel.getName());
//            // Populate hotel images
//            List<byte[]> hotelImages = hotelImagesMap.get(hotel.getHotelId());
//            if (hotelImages != null && !hotelImages.isEmpty()) {
//                List<String> imageUrls = new ArrayList<>();
//                for (byte[] image : hotelImages) {
//                    String imageUrl = convertToBase64(image);
//                    imageUrls.add(imageUrl);
//                }
//                hotelSearchDTO.setImages(imageUrls);
//            }
//            hotelSearchDTOs.add(hotelSearchDTO);
//        }
//        return hotelSearchDTOs;
//    }
//
//    private String convertToBase64(byte[] imageBytes) {
//        return Base64.getEncoder().encodeToString(imageBytes);
//    }

    public List<HotelSearchDTO> searchHotels(Date checkInDate, Date checkOutDate, String location) {
        // Step 2: Retrieve Contracts within the Date Range
        List<Contract> contracts = contractRepository.findByStartDateBeforeAndEndDateAfter(checkInDate, checkOutDate);

        Set<Long> hotelIds = new HashSet<>(); // To avoid duplicate hotels

        // Step 3 & 4: Retrieve Hotel Information and Eliminate Duplicate Hotels
        for (Contract contract : contracts) {
            hotelIds.add(contract.getHotel().getHotelId());
        }

        // Step 5: Filter Hotels by Location
        List<Hotel> hotelsInLocation = hotelRepository.findByCity(location)
                .stream()
                .filter(hotel -> hotelIds.contains(hotel.getHotelId()))
                .collect(Collectors.toList());

        // Step 6: Retrieve hotel images for each hotel
        Map<Long, List<HotelImages>> hotelImagesMap = new HashMap<>();
        for (Long hotelId : hotelIds) {
            Set<HotelImages> hotelImages = hotelImageRepository.findByHotel_HotelId(hotelId);
            List<HotelImages> images = new ArrayList<>(hotelImages);
            hotelImagesMap.put(hotelId, images);
        }

        return getHotelSearchDTOS(hotelsInLocation, hotelImagesMap);
    }

    private List<HotelSearchDTO> getHotelSearchDTOS(List<Hotel> hotels, Map<Long, List<HotelImages>> hotelImagesMap) {
        List<HotelSearchDTO> hotelSearchDTOs = new ArrayList<>();
        for (Hotel hotel : hotels) {
            HotelSearchDTO hotelSearchDTO = new HotelSearchDTO();
            // Populate hotel details
            hotelSearchDTO.setHotelId(hotel.getHotelId());
            hotelSearchDTO.setName(hotel.getName());
            hotelSearchDTO.setLocation(hotel.getCity());
            // Populate hotel images
            List<HotelImages> hotelImages = hotelImagesMap.get(hotel.getHotelId());
            if (hotelImages != null && !hotelImages.isEmpty()) {
                hotelSearchDTO.setImages(hotelImages);
            }
            hotelSearchDTOs.add(hotelSearchDTO);
        }
        return hotelSearchDTOs;
    }



//    public Hotel getHotelById(Long hotelId) {
//        Hotel hotel = new Hotel();
//        hotel = hotelRepository.findById(hotelId).orElse(null);
////        hotel.setHotelImages(hotelImageRepository.findByHotelId());
//        return hotel;
//    }

    public Hotel getHotelById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel != null) {
            Set<HotelImages> images = hotelImageRepository.findByHotel_HotelId(hotelId);
            hotel.setHotelImages(images);
        }
        return hotel;
    }

    public Long getIdByEmail(String email) {
        Hotel hotel = hotelRepository.getHotelByEmail(email);

        if (hotel != null) {
            return hotel.getHotelId();
        } else {
            return null;
        }
    }

    public SaveRoomType getBookedRoomByBookingId(Long bookingId){

        SaveRoomType saveRoomType = new SaveRoomType();
        saveRoomType = saveRoomTypeRepository.findSaveRoomTypeByBooking_BookingId(bookingId);

        return saveRoomType;
    }
}
