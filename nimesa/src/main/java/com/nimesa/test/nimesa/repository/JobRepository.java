package com.nimesa.test.nimesa.repository;

import com.nimesa.test.nimesa.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {}

