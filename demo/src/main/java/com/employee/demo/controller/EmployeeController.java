package com.employee.demo.controller;

import com.employee.demo.model.Employee;
import com.employee.demo.model.TrainingPeriod;
import com.employee.demo.service.EmployeeService;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService _employeeService;

    @PostMapping("/create/employee")
    public ResponseEntity<Object> createEmployee(@RequestBody Employee inEmployee) {
        return _employeeService.addEmployee(inEmployee);
    }

    @GetMapping("/employee/training-period/{employeeId}")
    public ResponseEntity<Object> getTrainingPeriodByEmployeeId(@PathVariable String employeeId) {
        return _employeeService.getTrainingPeriodsByEmployeeId(employeeId);
    }

    @DeleteMapping("employee/cancel/training-period/{employeeId}/{trainingPeriodId}")
    public ResponseEntity<Object> cancelTrainingPeriodByEmployeeId(@PathVariable String employeeId,
                                                                   @PathVariable Long trainingPeriodId) {
        return _employeeService.cancelTrainingPeriodByEmployeeId(employeeId, trainingPeriodId);
    }

    @DeleteMapping("employee/cancel/all-training-period/{employeeId}")
    public ResponseEntity<Object> cancelAllTrainingPeriodsByEmployeeId(@PathVariable String employeeId) {
        return _employeeService.cancelAllTrainingPeriodsByEmployeeId(employeeId);
    }

    @PutMapping("employee/update/training-period/{employeeId}")
    public ResponseEntity<Object> updateTrainingPeriodByEmployeeId(@PathVariable String employeeId,
                                                                   @RequestBody Set<TrainingPeriod> inTrainingPeriodList) {
        return _employeeService.updateTrainingPeriodsByEmployeeId(employeeId, inTrainingPeriodList);
    }
}
