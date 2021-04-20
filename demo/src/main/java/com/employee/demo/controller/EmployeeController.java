package com.employee.demo.controller;

import com.employee.demo.model.Employee;
import com.employee.demo.model.Schedule;
import com.employee.demo.service.EmployeeService;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @PostMapping("/employee/create")
    public ResponseEntity<Object> createEmployee(@RequestBody Employee inEmployee) {
        return _employeeService.addEmployee(inEmployee);
    }

    @GetMapping("/schedule/{employeeId}")
    public ResponseEntity<Object> getScheduleByEmployeeId(@PathVariable String employeeId) {
        return _employeeService.getScheduleByEmployeeId(employeeId);
    }

    @DeleteMapping("/cancel/schedule/{employeeId}/{scheduleId}")
    public ResponseEntity<Object> cancelScheduleByEmployeeId(@PathVariable String employeeId,
                                                             @PathVariable Long scheduleId) {
        return _employeeService.cancelScheduleByEmployeeId(employeeId, scheduleId);
    }

    @DeleteMapping("/cancel/schedule/{employeeId}")
    public ResponseEntity<Object> cancelAllScheduleByEmployeeId(@PathVariable String employeeId) {
        return _employeeService.cancelAllScheduleByEmployeeId(employeeId);
    }

    @PutMapping("update/schedule/{employeeId}")
    public ResponseEntity<Object> updateScheduleByEmployeeId(@PathVariable String employeeId,
                                                             @RequestBody Set<Schedule> inScheduleList) {
        return _employeeService.updateScheduleByEmployeeId(employeeId, inScheduleList);
    }

    @GetMapping("schedule/date/{date}")
    public ResponseEntity<Object> getScheduleOnDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return _employeeService.getSchedulesOnDate(date);
    }
}
