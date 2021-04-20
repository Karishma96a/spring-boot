package com.employee.demo.service;

import com.employee.demo.model.Employee;
import com.employee.demo.model.TrainingPeriod;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.repository.TrainingPeriodRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository _employeeRepository;

    @Autowired
    private TrainingPeriodRepository _trainingPeriodRepository;

    @Autowired
    private TrainingPeriodService _trainingPeriodService;

    @Transactional
    public ResponseEntity<Object> addEmployee(final Employee inEmployee) {
        final Employee employee = new Employee();
        employee.setEmployeeId(inEmployee.getEmployeeId());

        final boolean isTrainingPeriodPersisted = _trainingPeriodService.saveTrainingPeriod(inEmployee.getTrainingPeriods());
        if (!isTrainingPeriodPersisted) {
            return FAILED_CREATING_EMPLOYEES_AND_TRAINING_PERIOD;
        }

        employee.setTrainingPeriods(inEmployee.getTrainingPeriods());
        _employeeRepository.save(employee);
        return ResponseEntity.accepted().body("Successfully Created Employee and training Period");
    }

    @Transactional
    public ResponseEntity<Object> getTrainingPeriodsByEmployeeId(final String inEmployeeId) {
        final Optional<Employee> employee = _employeeRepository.findById(inEmployeeId);
        return (employee.isEmpty()) ? ResponseEntity.noContent().build() : ResponseEntity.accepted().body(employee.get().getTrainingPeriods());
    }

    @Transactional
    public ResponseEntity<Object> cancelTrainingPeriodByEmployeeId(final String inEmployeeId, final Long inTrainingPeriodId) {
        final Optional<Employee> employeeEntity = _employeeRepository.findById(inEmployeeId);

        if (employeeEntity.isEmpty()) {
            return EMPLOYEE_NOT_PRESENT_FAILURE;
        }

        final Employee employee = employeeEntity.get();
        final Set<TrainingPeriod> trainingPeriodSet = employee.getTrainingPeriods();
        final Optional<TrainingPeriod> trainingPeriod = _trainingPeriodRepository.findById(inTrainingPeriodId);

        if (trainingPeriod.isPresent()) {
            trainingPeriodSet.remove(trainingPeriod.get());
            employee.setTrainingPeriods(trainingPeriodSet);
            return ResponseEntity.accepted().body("Successfully deleted training period");
        }

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Object> cancelAllTrainingPeriodsByEmployeeId(final String inEmployeeId) {
        final Employee employee = _employeeRepository.findById(inEmployeeId).orElse(null);

        if (employee == null) {
            return EMPLOYEE_NOT_PRESENT_FAILURE;
        }

        employee.getTrainingPeriods().clear();
        return ResponseEntity.accepted().body("Successfully deleted all training periods");
    }

    @Transactional
    public ResponseEntity<Object> updateTrainingPeriodsByEmployeeId(final String inEmployeeId, final Set<TrainingPeriod> inTrainingPeriodList) {
        final Employee employee = _employeeRepository.findById(inEmployeeId).orElse(null);

        if (employee == null) {
            return EMPLOYEE_NOT_PRESENT_FAILURE;
        }

        employee.getTrainingPeriods().clear();

        for (final TrainingPeriod trainingPeriod : inTrainingPeriodList) {
            _trainingPeriodRepository.save(trainingPeriod);
        }
        employee.getTrainingPeriods().addAll(inTrainingPeriodList);

        _employeeRepository.saveAndFlush(employee);
        return ResponseEntity.accepted().body("Updated successfully");
    }

    private static final ResponseEntity<Object> EMPLOYEE_NOT_PRESENT_FAILURE =
            ResponseEntity.unprocessableEntity().body("Failed because employee is not present");
    private static final ResponseEntity<Object> FAILED_CREATING_EMPLOYEES_AND_TRAINING_PERIOD =
            ResponseEntity.unprocessableEntity().body("Failed creating employees and training period");
}
