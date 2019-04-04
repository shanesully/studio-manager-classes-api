package com.studiomanager.studioapi.services.impl;

import com.studiomanager.studioapi.domain.Booking;
import com.studiomanager.studioapi.services.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("bookingService")
public class BookingServiceImpl implements BookingService {
  static final String uri = "http://localhost:8090/bookings";

  public String getBookings() {
    RestTemplate restTemplate = new RestTemplate();

    return restTemplate.getForObject(uri, String.class);
  }

  public Booking getBookingById(int id) {
    RestTemplate restTemplate = new RestTemplate();

    return restTemplate.getForObject(uri + "/" + id, Booking.class);
  }
}
