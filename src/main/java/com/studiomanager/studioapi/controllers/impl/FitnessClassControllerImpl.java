package com.studiomanager.studioapi.controllers.impl;

import com.studiomanager.studioapi.controllers.FitnessClassController;
import com.studiomanager.studioapi.domain.ErrorEntity;
import com.studiomanager.studioapi.domain.FitnessClass;
import com.studiomanager.studioapi.services.impl.BookingServiceImpl;
import com.studiomanager.studioapi.services.impl.FitnessClassMockServiceImpl;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("fitnessClassController")
public class FitnessClassControllerImpl implements FitnessClassController {

  @Autowired
  private FitnessClassMockServiceImpl fitnessClassMockService;

  @Autowired
  private BookingServiceImpl bookingService;

  @RequestMapping(value="/classes", method=RequestMethod.POST)
  public ResponseEntity<Object> createFitnessClass(@RequestBody FitnessClass fitnessClass) {
    fitnessClass.setId(fitnessClassMockService.getMockFitnessClasses().size() + 1);

    fitnessClassMockService.getMockFitnessClasses()
        .put(fitnessClass.getId(), fitnessClass);

    return new ResponseEntity<>("Fitness class created", HttpStatus.CREATED);
  }

  @RequestMapping(value="/classes")
  public ResponseEntity<Object> getFitnessClasses() {
    return new ResponseEntity<>(fitnessClassMockService.getMockFitnessClasses(), HttpStatus.OK);
  }

  @RequestMapping(value="/classes/{id}")
  public ResponseEntity<Object> getFitnessClass(@PathVariable("id") int id) {
    return new ResponseEntity<>(fitnessClassMockService.getMockFitnessClasses().get(id), HttpStatus.OK);
  }

  @RequestMapping(value="/classes/{id}", method=RequestMethod.PUT)
  public ResponseEntity<Object> updateFitnessClass(@PathVariable("id") int id, @RequestBody FitnessClass fitnessClass) {
    fitnessClass.setId(id);

    fitnessClassMockService.getMockFitnessClasses()
        .remove(id); //TODO Minor - Add logging to this method

    fitnessClassMockService.getMockFitnessClasses()
        .put(id, fitnessClass); //TODO Minor - Add logging to this method

    return new ResponseEntity<>("Fitness class updated successfully", HttpStatus.OK);
  }

  @RequestMapping(value="/classes/{id}", method=RequestMethod.DELETE)
  public ResponseEntity<Object> deleteFitnessClass(@PathVariable("id") int id) {
    if(!bookingsAssigned(id)) {
      fitnessClassMockService.getMockFitnessClasses().remove(id); //TODO Minor - Add logging to this method

      return new ResponseEntity<>("Fitness class successfully deleted", HttpStatus.OK);
    } else {
      ErrorEntity errorEntity = new ErrorEntity();

      errorEntity.setReason("Fitness class Deletion failed - Existing Bookings");

      return new ResponseEntity<>(errorEntity, HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value="/classes/{id}/increment")
  public ResponseEntity<Object> incrementFitnessClassCapacity(@PathVariable("id") int id) {
    fitnessClassMockService.getMockFitnessClasses().get(id).incrementCapacity();

    String classId = fitnessClassMockService.getMockFitnessClasses().get(id).toString();

    return new ResponseEntity<>("Class: " + classId + " successfully incremented", HttpStatus.OK);
  }

  @RequestMapping(value="/classes/{id}/decrement")
  public ResponseEntity<Object> decrementFitnessClassCapacity(@PathVariable("id") int id) {
    fitnessClassMockService.getMockFitnessClasses().get(id).decrementCapacity();

    return new ResponseEntity<>(fitnessClassMockService.getMockFitnessClasses().get(id), HttpStatus.OK);
  }

  private boolean bookingsAssigned(int id) {
    return bookingService.getBookingById(id) != null ? true : false;
  }
}
