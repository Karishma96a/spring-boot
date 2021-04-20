package com.employee.demo.controller;

import com.employee.demo.service.TrainingPeriodService;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainingPeriodController {

    @Autowired
    private TrainingPeriodService _trainingPeriodService;

    @GetMapping("training-period/date/{date}")
    public ResponseEntity<Object> getTrainingPeriodsOnDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return _trainingPeriodService.getTrainingPeriodsOnDate(date);
    }
}
