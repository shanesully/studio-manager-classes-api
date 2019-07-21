package com.studiomanager.studioapi.services;

import com.studiomanager.studioapi.SLF4JExample;
import com.studiomanager.studioapi.domain.FitnessClass;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FitnessClassService {

  private static Map<Integer, FitnessClass> fitnessClasses = new HashMap<>();
  private static final Logger LOGGER = LoggerFactory.getLogger(SLF4JExample.class);

  static {
    //TODO - Replace with IMDB
    FitnessClass fitnessClass = new FitnessClass.Builder(1)
        .withCapacity(9)
        .withClassName("Pilates")
        .withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(1))
        .build();

    fitnessClasses.put(fitnessClass.getId(), fitnessClass);
  }

  public FitnessClassService() {}

  public Map<Integer, FitnessClass> getClasses() {
    return fitnessClasses;
  }

  public FitnessClass getClassById(int id) {
    return fitnessClasses.get(id);
  }

  public static Map<Integer, FitnessClass> getFitnessClasses() {
    return fitnessClasses;
  }

  public static void setFitnessClasses(
      Map<Integer, FitnessClass> fitnessClasses) {
    FitnessClassService.fitnessClasses = fitnessClasses;
  }

  public static Logger getLOGGER() {
    return LOGGER;
  }

  public void addClass(FitnessClass fitnessClass) {
    fitnessClasses.put(fitnessClasses.size() + 1, fitnessClass);
    LOGGER.info("Added fitness class: " + fitnessClass.getId());
  }

  public void removeClassById(int id) {
    fitnessClasses.remove(id);
    LOGGER.info("Removed fitness class: " + id);
  }

  public void updateClass(FitnessClass fitnessClass) {
    removeClassById(fitnessClass.getId());
    addClass(fitnessClass);
  }
}
