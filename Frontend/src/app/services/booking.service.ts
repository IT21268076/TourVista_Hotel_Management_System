import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environment';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private backendUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  confirmBooking(roomTypeId: number, seasonId: number, bookingData: any): Observable<any> {
    const url = `${this.backendUrl}/booking`; 
    return this.http.post<any>(`${this .backendUrl}/booking`, bookingData, { 
      params: { 
        roomTypeId: roomTypeId.toString(), 
        seasonId: seasonId.toString() 
      } 
    });
  }

  // getBookingsForUser(userId: string): Observable<Booking[]> {
  //   return this.http.get<Booking[]>(`${this.baseUrl}/bookings?userId=${userId}`);
  // }

}
