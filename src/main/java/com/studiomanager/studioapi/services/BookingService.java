package com.studiomanager.studioapi.services;

import com.studiomanager.studioapi.domain.Booking;

public interface BookingService {
  public String getBookings();
  public Booking getBookingById(int id);
}
