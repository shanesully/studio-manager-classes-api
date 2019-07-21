package com.studiomanager.studioapi.services;

import com.studiomanager.studioapi.domain.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookingService {

  private static final String HOST = "http://localhost";
  private static final String PORT = "8090";
  private static final String BOOKINGS_ENDPOINT = "/bookings";

  private static String URI = HOST + ":" + PORT + BOOKINGS_ENDPOINT;
  RestTemplate restTemplate = new RestTemplate();

  public String getBookings() {
    return restTemplate.getForObject(URI, String.class);
  }

  public Booking getBookingById(int id) {
    return restTemplate.getForObject(URI + "/" + id, Booking.class);
  }
}
