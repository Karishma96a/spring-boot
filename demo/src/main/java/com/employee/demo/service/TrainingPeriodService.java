package com.employee.demo.service;

import com.employee.demo.model.DaysOfWeek;
import com.employee.demo.model.TrainingPeriod;
import com.employee.demo.repository.TrainingPeriodRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TrainingPeriodService {

    @Autowired
    private TrainingPeriodRepository _trainingPeriodRepository;

    @Transactional
    public ResponseEntity<Object> getTrainingPeriodsOnDate(LocalDate inDate) {
        final List<TrainingPeriod> trainingPeriods = _trainingPeriodRepository.findAll();
        final Set<TrainingPeriod> result = new HashSet<>();

        for (final TrainingPeriod trainingPeriod : trainingPeriods) {

            if (isDateAfterEndDate(inDate, trainingPeriod) ||
                    isDateBeforeStartDate(inDate, trainingPeriod) ||
                    isTrainingPeriodNotOnRepeatAndNotOnStartDate(inDate, trainingPeriod)) {
                continue;
            }

            if (isNotOnRepeatButOnStartDate(inDate, trainingPeriod)) {
                result.add(trainingPeriod);
            }

            if (trainingPeriod.isRepeat()
                    && (isFrequencyDaily(trainingPeriod.getFrequency())
                    || conditionForMonthlyFrequency(inDate, trainingPeriod)
                    || conditionForWeekdaysCondition(inDate, trainingPeriod)
                    || conditionForWeeklyCondition(inDate, trainingPeriod))) {
                result.add(trainingPeriod);
            }
        }

        return ResponseEntity.accepted().body(result);
    }

    private boolean conditionForWeekdaysCondition(LocalDate inDate, TrainingPeriod inTrainingPeriod) {
        return DaysOfWeek.WEEKDAYS.equals(inTrainingPeriod.getFrequency())
                && inDate.getDayOfWeek().getValue() >= 1 && inDate.getDayOfWeek().getValue() <= 5;
    }

    private boolean conditionForWeeklyCondition(LocalDate inDate, TrainingPeriod inTrainingPeriod) {
        return DaysOfWeek.WEEKLY.equals(inTrainingPeriod.getFrequency())
                && inTrainingPeriod.getStartDate().getDayOfWeek() == inDate.getDayOfWeek();
    }

    private boolean conditionForMonthlyFrequency(LocalDate inDate, TrainingPeriod inTrainingPeriod) {
        return DaysOfWeek.MONTHLY.equals(inTrainingPeriod.getFrequency())
                && inTrainingPeriod.getStartDate().getDayOfMonth() == inDate.getDayOfMonth();
    }

    private boolean isFrequencyDaily(DaysOfWeek inDaysOfWeek) {
        return DaysOfWeek.DAILY.equals(inDaysOfWeek);
    }

    private boolean isNotOnRepeatButOnStartDate(LocalDate inDate, TrainingPeriod inTrainingPeriod) {
        return !inTrainingPeriod.isRepeat()
                && inDate.getDayOfYear() == inTrainingPeriod.getStartDate().getDayOfYear()
                && inDate.getYear() == inTrainingPeriod.getStartDate().getYear();
    }

    private boolean isTrainingPeriodNotOnRepeatAndNotOnStartDate(final LocalDate inDate, final TrainingPeriod inTrainingPeriod) {
        return inDate.getDayOfMonth() > inTrainingPeriod.getStartDate().getDayOfMonth() && !inTrainingPeriod.isRepeat();
    }

    private boolean isDateAfterEndDate(final LocalDate inDate, final TrainingPeriod inTrainingPeriod) {
        return inDate.isAfter(inTrainingPeriod.getEndDate());
    }

    private boolean isDateBeforeStartDate(LocalDate inDate, TrainingPeriod trainingPeriod) {
        return inDate.isBefore(trainingPeriod.getStartDate());
    }
}
