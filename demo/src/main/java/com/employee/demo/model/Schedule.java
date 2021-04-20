package com.employee.demo.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long scheduleId;

    LocalDate startDate;

    LocalDate endDate;

    String time;

    int duration;

    boolean repeat;

    @Enumerated(EnumType.STRING)
    DaysOfWeek frequency;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long inScheduleId) {
        scheduleId = inScheduleId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate inStartDate) {
        startDate = inStartDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate inEndDate) {
        endDate = inEndDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String inTime) {
        time = inTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int inDuration) {
        duration = inDuration;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean inRepeat) {
        repeat = inRepeat;
    }

    public DaysOfWeek getFrequency() {
        return frequency;
    }

    public void setFrequency(DaysOfWeek inFrequency) {
        frequency = inFrequency;
    }
}
