package com.nimesa.test.nimesa.repository;

import com.nimesa.test.nimesa.model.ServiceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceResultRepository extends JpaRepository<ServiceResult, Long> {
    List<ServiceResult> findByService(String serviceName);
    List<ServiceResult> findByServiceAndPattern(String serviceName, String pattern);
}