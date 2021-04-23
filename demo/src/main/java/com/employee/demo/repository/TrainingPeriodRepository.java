package com.employee.demo.repository;

import com.employee.demo.model.TrainingPeriod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingPeriodRepository extends JpaRepository<TrainingPeriod, Long> {
}
