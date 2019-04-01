package com.studiomanager.studioapi.services.impl;

import com.studiomanager.studioapi.domain.FitnessClass;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.studiomanager.studioapi.services.FitnessClassMockService;
import org.springframework.stereotype.Service;

@Service("fitnessClassMockService")
public class FitnessClassMockServiceImpl implements FitnessClassMockService {

  private static Map<Integer, FitnessClass> fitnessClassMap = new HashMap<>();

  static {
    FitnessClass fitnessClass = new FitnessClass();
    fitnessClass.setId(1);
    fitnessClass.setCapacity(9);
    fitnessClass.setClassName("Pilates");
    fitnessClass.setStartDate(LocalDate.now());
    fitnessClass.setEndDate(LocalDate.now().plusDays(1));

    fitnessClassMap.put(fitnessClass.getId(), fitnessClass);
  }

  public Map<Integer, FitnessClass> getMockFitnessClasses() {
    return fitnessClassMap;
  }

  public FitnessClass getFitnessClassById(int id) {
    return fitnessClassMap.get(id);
  }
}
