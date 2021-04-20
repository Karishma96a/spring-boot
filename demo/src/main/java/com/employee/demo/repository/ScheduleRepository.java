package com.employee.demo.repository;

import com.employee.demo.model.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
