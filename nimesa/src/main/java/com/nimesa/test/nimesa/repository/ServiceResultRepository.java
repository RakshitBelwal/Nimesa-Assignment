package com.nimesa.test.nimesa.repository;

import com.nimesa.test.nimesa.model.ServiceResult;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceResultRepository extends CrudRepository<ServiceResult, Long> {
    List<ServiceResult> findByService(String serviceName);
    List<ServiceResult> findByJobId(Long id);
}