package com.studiomanager.studioapi.controllers;

import com.studiomanager.studioapi.domain.FitnessClass;
import org.springframework.http.ResponseEntity;

public interface FitnessClassController {
    public ResponseEntity<Object> getFitnessClasses();
    public ResponseEntity<Object> getFitnessClass(int id);
    public ResponseEntity<Object> createFitnessClass(FitnessClass fitnessClass);
    public ResponseEntity<Object> updateFitnessClass(int id, FitnessClass fitnessClass);
    public ResponseEntity<Object> deleteFitnessClass(int id);
}
