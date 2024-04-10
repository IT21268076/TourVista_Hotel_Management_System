import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from '../../../services/hotel.service'; // Import your service
import { Router } from '@angular/router'; // Import Router for navigation

@Component({
  selector: 'app-search-hotel',
  templateUrl: './search-hotel.component.html',
  styleUrls: ['./search-hotel.component.css']
})
export class SearchHotelComponent implements OnInit {
  location: string = '';
  checkInDate: string = '';
  checkOutDate: string = '';
  guests: number = 1;
  cards: any[] = [];
  image: any;

  constructor(private router: Router, private route: ActivatedRoute, private hotelService: HotelService) { }

  ngOnInit(): void {
    // Extract parameters from the URL
    this.route.queryParams.subscribe(params => {
      this.location = params['location'];
      this.checkInDate = params['checkInDate'];
      this.checkOutDate = params['checkOutDate'];
      //const guests = params['guests'];

      // Call the method to fetch hotels with parameters
      this.fetchHotels(this.location, this.checkInDate, this.checkOutDate);
      // this.redirectToHotelDetails('hotelId', checkInDate, checkOutDate);
    });
  }

  // fetchHotels(location: string, checkInDate: string, checkOutDate: string) {
  //   // Assuming you have a method in your service to fetch hotels with parameters
  //   this.hotelService.getHotels(location, checkInDate, checkOutDate).subscribe(
  //     (response: any) => {
  //       console.log(response);
        
  //       // Assuming response is an array of hotel objects
  //       this.cards = response.map((hotel: any) => {
  //         return {
  //           hotelId: hotel.hotelId, 
  //           imageUrl: hotel.imageUrl,
  //           name: hotel.name,
  //           address: hotel.address,
  //           description: hotel.description
  //         };
  //       });
  //     },
  //     (error: any) => {
  //       console.error('Error fetching hotels:', error);
  //     }
  //   );
  // }

  fetchHotels(location: string, checkInDate: string, checkOutDate: string) {
    this.hotelService.getHotels(location, checkInDate, checkOutDate).subscribe(
      (response: any[]) => {
        console.log(response);
        
        // Assuming response is an array of hotel objects
        this.cards = response.map((hotel: any) => {
          return {
            hotelId: hotel.hotelId, 
            image: hotel.images.length > 0 ? hotel.images[0].image : null, // Assuming the first image URL is used
            name: hotel.name,
            address: hotel.address,
            description: hotel.description
          };
        });
        
        
      },
      (error: any) => {
        console.error('Error fetching hotels:', error);
      }
    );
  }
  

  redirectToHotelDetails(hotelId: string) {
    // Navigate to hotel-details page with hotelId, checkInDate, and checkOutDate as query parameters
    this.router.navigate(['/hotel-details', hotelId], {
      queryParams: {
        checkInDate: this.checkInDate,
        checkOutDate: this.checkOutDate
      }
    });
  }
}
