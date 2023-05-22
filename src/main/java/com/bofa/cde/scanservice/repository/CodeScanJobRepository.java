package com.bofa.cde.scanservice.repository;

import com.bofa.cde.scanservice.model.CodeScanJob;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CodeScanJobRepository extends CrudRepository<CodeScanJob, Long> {

    List<CodeScanJob> findByStatus(String status);
}
