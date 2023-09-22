package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.AdmissionPolicy;
import org.springframework.data.repository.CrudRepository;

public interface AdmissionPolicyRepository extends CrudRepository<AdmissionPolicy, Long> {
}
