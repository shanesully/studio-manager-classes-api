package com.studiomanager.studioapi.controllers;

import com.studiomanager.studioapi.domain.FitnessClass;
import org.springframework.http.ResponseEntity;

public interface FitnessClassController {
    public ResponseEntity<Object> getFitnessClasses();
    public ResponseEntity<Object> getFitnessClass(int id);
    public ResponseEntity<Object> createUpdateFitnessClass(FitnessClass fitnessClass);
    public ResponseEntity<Object> deleteFitnessClass(int id);
}
