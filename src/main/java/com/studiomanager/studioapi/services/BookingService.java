package com.studiomanager.studioapi.services;

import com.studiomanager.studioapi.domain.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookingService {
  @Value("${port}")
  private String PORT;

  @Value("${host}")
  private String HOST;

  @Value("${bookingsEndpoint}")
  private String BOOKINGS_ENDPOINT;

  final String URI = HOST + ":" + PORT + BOOKINGS_ENDPOINT;
  RestTemplate restTemplate = new RestTemplate();

  public String getBookings() {
    return restTemplate.getForObject(URI, String.class);
  }

  public Booking getBookingById(int id) {
    return restTemplate.getForObject(URI + "/" + id, Booking.class);
  }
}
