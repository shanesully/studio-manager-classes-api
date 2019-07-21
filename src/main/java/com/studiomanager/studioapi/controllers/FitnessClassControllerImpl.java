package com.studiomanager.studioapi.controllers;

import com.studiomanager.studioapi.domain.FitnessClass;
import com.studiomanager.studioapi.services.BookingService;
import com.studiomanager.studioapi.services.FitnessClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FitnessClassControllerImpl implements FitnessClassController {

  @Autowired
  private FitnessClassService fitnessClassService;

  @Autowired
  private BookingService bookingService;
  
  @RequestMapping(value="/classes", method={ RequestMethod.POST, RequestMethod.PUT })
  public ResponseEntity<Object> createUpdateFitnessClass(@RequestBody FitnessClass fitnessClass) {
    fitnessClassService.createUpdateClass(fitnessClass);

    return new ResponseEntity<>("Fitness classes updated", HttpStatus.CREATED);
  }

  @RequestMapping(value="/classes")
  public ResponseEntity<Object> getFitnessClasses() {
    return new ResponseEntity<>(fitnessClassService.getClasses(), HttpStatus.OK);
  }

  @RequestMapping(value="/classes/{id}")
  public ResponseEntity<Object> getFitnessClass(@PathVariable("id") int id) {
    return new ResponseEntity<>(fitnessClassService.getClassById(id), HttpStatus.OK);
  }

  @RequestMapping(value="/classes/{id}", method=RequestMethod.DELETE)
  public ResponseEntity<Object> deleteFitnessClass(@PathVariable("id") int id) {
    if(!bookingsExistForClass(id)) {
      fitnessClassService.removeClassById(id);

      return new ResponseEntity<>("Fitness class " + id + " successfully deleted", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Fitness class " + id + "deletion failed - Bookings exist", HttpStatus.OK);
    }
  }

  private boolean bookingsExistForClass(int id) {
    return bookingService.getBookingById(id) != null ? true : false;
  }
}
