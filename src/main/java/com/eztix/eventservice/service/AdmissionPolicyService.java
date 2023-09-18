package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.AdmissionPolicy;
import com.eztix.eventservice.repository.AdmissionPolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdmissionPolicyService {
    private final AdmissionPolicyRepository admissionPolicyRepository;
    public AdmissionPolicyService (AdmissionPolicyRepository admissionPolicyRepository){
        this.admissionPolicyRepository = admissionPolicyRepository;
    }

    public AdmissionPolicy getAdmissionPolicyById(Long admissionPolicyId){
        return admissionPolicyRepository.findById(admissionPolicyId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("admission policy with id %d does not exist", admissionPolicyId)));
    }

    public Iterable<AdmissionPolicy> getAllAdmissionPolicy(){
        return admissionPolicyRepository.findAll();
    }

    public AdmissionPolicy addNewAdmissionPolicy(AdmissionPolicy admissionPolicy){
        return admissionPolicyRepository.save(admissionPolicy);
    }

    @Transactional
    public AdmissionPolicy updateAdmissionPolicy(AdmissionPolicy admissionPolicy){
        if (admissionPolicy.getId() == null){
            throw new RequestValidationException("admission policy id cannot be null.");
        }

        admissionPolicyRepository.findById(admissionPolicy.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("admission policy with id %d does not exist", admissionPolicy.getId())));

        return admissionPolicyRepository.save(admissionPolicy);
    }




}