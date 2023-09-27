package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.AdmissionPolicy;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AdmissionPolicyRepository extends CrudRepository<AdmissionPolicy, Long> {
    Optional<List<AdmissionPolicy>> findAllByEventId(Long eventId);
}
