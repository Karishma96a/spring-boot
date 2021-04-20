package com.employee.demo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @Column(name = "employee_id")
    String employeeId;

    @OneToMany(targetEntity = Schedule.class, cascade = CascadeType.PERSIST, orphanRemoval = true)
    Set<Schedule> trainingPeriod;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String inEmployeeId) {
        employeeId = inEmployeeId;
    }

    public Set<Schedule> getTrainingPeriod() {
        return trainingPeriod;
    }

    public void setTrainingPeriod(Set<Schedule> inTrainingPeriod) {
        trainingPeriod = inTrainingPeriod;
    }
}
