package com.employee.demo.service;

import com.employee.demo.model.DaysOfWeek;
import com.employee.demo.model.Employee;
import com.employee.demo.model.Schedule;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.repository.ScheduleRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
    private ScheduleRepository _scheduleRepository;

    @Transactional
    public ResponseEntity<Object> addEmployee(final Employee inEmployee) {
        final Employee employee = new Employee();
        employee.setEmployeeId(inEmployee.getEmployeeId());

        final boolean isTrainingPeriodPersisted = saveTrainingPeriod(inEmployee);
        if (!isTrainingPeriodPersisted) {
            return FAILED_CREATING_EMPLOYEES_AND_TRAINING_PERIOD;
        }

        employee.setTrainingPeriod(inEmployee.getTrainingPeriod());
        _employeeRepository.save(employee);
        return ResponseEntity.accepted().body("Successfully Created Employee and training Period");
    }

    @Transactional
    public ResponseEntity<Object> getScheduleByEmployeeId(final String inEmployeeId) {
        final Optional<Employee> employee = _employeeRepository.findById(inEmployeeId);
        return (employee.isEmpty()) ? ResponseEntity.noContent().build() : ResponseEntity.accepted().body(employee.get().getTrainingPeriod());
    }

    @Transactional
    public ResponseEntity<Object> cancelScheduleByEmployeeId(final String inEmployeeId, final Long inScheduleId) {
        final Optional<Employee> employeeEntity = _employeeRepository.findById(inEmployeeId);

        if (employeeEntity.isEmpty()) {
            return EMPLOYEE_ABSENT_FAILURE;
        }

        final Employee employee = employeeEntity.get();
        final Set<Schedule> scheduleSet = employee.getTrainingPeriod();
        final Optional<Schedule> schedule = _scheduleRepository.findById(inScheduleId);

        if (schedule.isPresent()) {
            scheduleSet.remove(schedule.get());
            employee.setTrainingPeriod(scheduleSet);
            return ResponseEntity.accepted().body("Successfully deleted schedule");
        }

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Object> cancelAllScheduleByEmployeeId(String inEmployeeId) {
        final Employee employee = _employeeRepository.findById(inEmployeeId).orElse(null);

        if (employee == null) {
            return EMPLOYEE_ABSENT_FAILURE;
        }

        employee.getTrainingPeriod().clear();
        return ResponseEntity.accepted().body("Successfully deleted all schedules");
    }

    @Transactional
    public ResponseEntity<Object> updateScheduleByEmployeeId(String inEmployeeId, Set<Schedule> inScheduleList) {
        final Employee employee = _employeeRepository.findById(inEmployeeId).orElse(null);

        if (employee == null) {
            return EMPLOYEE_ABSENT_FAILURE;
        }

        employee.getTrainingPeriod().clear();

        for (final Schedule schedule : inScheduleList) {
            _scheduleRepository.save(schedule);
        }
        employee.getTrainingPeriod().addAll(inScheduleList);

        _employeeRepository.saveAndFlush(employee);
        return ResponseEntity.accepted().body("Updated successfully");
    }

    @Transactional
    public ResponseEntity<Object> getSchedulesOnDate(LocalDate inDate) {
        final List<Schedule> schedules = _scheduleRepository.findAll();
        final Set<Schedule> result = new HashSet<>();

        for (final Schedule schedule : schedules) {

            if (isDateAfterEndDate(inDate, schedule) || isScheduleNotOnRepeatAndNotOnStartDate(inDate, schedule)) {
                continue;
            }

            if (isNotOnRepeatButOnStartDate(inDate, schedule)) {
                result.add(schedule);
            }

            if (schedule.isRepeat()
                    && (isFrequencyDaily(schedule.getFrequency())
                    || conditionForMonthlyFrequency(inDate, schedule)
                    || conditionForWeekdaysCondition(inDate, schedule)
                    || conditionForWeeklyCondition(inDate, schedule))) {
                result.add(schedule);
            }
        }

        return ResponseEntity.accepted().body(result);
    }

    private boolean conditionForWeekdaysCondition(LocalDate inDate, Schedule schedule) {
        return DaysOfWeek.WEEKDAYS.equals(schedule.getFrequency())
                && inDate.getDayOfWeek().getValue() >= 2 && inDate.getDayOfWeek().getValue() <= 6;
    }

    private boolean conditionForWeeklyCondition(LocalDate inDate, Schedule schedule) {
        return DaysOfWeek.WEEKLY.equals(schedule.getFrequency())
                && schedule.getStartDate().getDayOfWeek() == inDate.getDayOfWeek();
    }

    private boolean conditionForMonthlyFrequency(LocalDate inDate, Schedule schedule) {
        return DaysOfWeek.MONTHLY.equals(schedule.getFrequency())
                && schedule.getStartDate().getDayOfMonth() == inDate.getDayOfMonth();
    }

    private boolean isFrequencyDaily(DaysOfWeek inDaysOfWeek) {
        return DaysOfWeek.DAILY.equals(inDaysOfWeek);
    }

    private boolean isNotOnRepeatButOnStartDate(LocalDate inDate, Schedule schedule) {
        return !schedule.isRepeat()
                && inDate.getDayOfYear() == schedule.getStartDate().getDayOfYear()
                && inDate.getYear() == schedule.getStartDate().getYear();
    }

    private boolean isScheduleNotOnRepeatAndNotOnStartDate(final LocalDate inDate, final Schedule schedule) {
        return inDate.getDayOfMonth() > schedule.getStartDate().getDayOfMonth() && !schedule.isRepeat();
    }

    private boolean isDateAfterEndDate(final LocalDate inDate, final Schedule schedule) {
        return inDate.isAfter(schedule.getEndDate());
    }

    private boolean saveTrainingPeriod(final Employee inEmployee) {
        for (final Schedule schedule : inEmployee.getTrainingPeriod()) {
            final Schedule persistedSchedule = _scheduleRepository.save(schedule);
            if (_scheduleRepository.findById(persistedSchedule.getScheduleId()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static final ResponseEntity<Object> EMPLOYEE_ABSENT_FAILURE =
            ResponseEntity.unprocessableEntity().body("Failed because employee is not present");
    private static final ResponseEntity<Object> FAILED_CREATING_EMPLOYEES_AND_TRAINING_PERIOD =
            ResponseEntity.unprocessableEntity().body("Failed creating employees and training period");
}
