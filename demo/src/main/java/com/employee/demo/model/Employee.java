package com.employee.demo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @Column(name = "employee_id")
    String employeeId;

    @OneToMany(targetEntity = TrainingPeriod.class, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<TrainingPeriod> trainingPeriods;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(final String inEmployeeId) {
        employeeId = inEmployeeId;
    }

    public Set<TrainingPeriod> getTrainingPeriods() {
        return trainingPeriods;
    }

    public void setTrainingPeriods(final Set<TrainingPeriod> inTrainingPeriod) {
        trainingPeriods = inTrainingPeriod;
    }
}
