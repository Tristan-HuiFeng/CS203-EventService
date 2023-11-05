package com.eztix.eventservice.service;

import com.eztix.eventservice.dto.request.NewAdmissionPolicy;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.AdmissionPolicy;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.AdmissionPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdmissionPolicyService {
    private final AdmissionPolicyRepository admissionPolicyRepository;

    /**
     * Retrieve admission policies based on eventId.
     * 
     * @param eventId a long value representing the unique identifier of the Event of the AdmissionPolicy to retrieve.
     * @return a list of retrieved AdmissionPolicy.
     */
    public List<AdmissionPolicy> getAllAdmissionPolicyByEventId(Long eventId){
        return admissionPolicyRepository.findAllByEventIdOrderByPolicyOrder(eventId).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("event with id %d does not have admission policy", eventId))
                );
    }

    /**
     * Create an admission policy.
     * 
     * @param event an Event object associated with the AdmissionPolicy to be created.
     * @param admissionPolicy an AdmissionPolicy object to associate with the Event.
     * @return the created AdmissionPolicy object.
     */
    public AdmissionPolicy addNewAdmissionPolicy(Event event, AdmissionPolicy admissionPolicy){
        admissionPolicy.setEvent(event);
        return admissionPolicyRepository.save(admissionPolicy);
    }

    /**
     * Update an admission policy.     
     * If "id" is null, throw a RequestValidationException.
     * If there is no admission policy with given "id", throw a ResourceNotFoundException.
     * 
     * @param admissionPolicy an AdmissionPolicy object containing the new AdmissionPolicy info to be updated.
     * @return the updated AdmissionPolicy object.
     */
    @Transactional
    public AdmissionPolicy updateAdmissionPolicy(AdmissionPolicy admissionPolicy){
        if (admissionPolicy.getId() == null){
            throw new RequestValidationException("admission policy id cannot be null.");
        }

        admissionPolicyRepository.findById(admissionPolicy.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("admission policy with id %d does not exist", admissionPolicy.getId())));

        return admissionPolicyRepository.save(admissionPolicy);
    }

    /**
     * Delete an admission policy.
     * If "id" is null, throw a RequestValidationException.
     * 
     * @param admissionPolicyId a long value representing the unique identifier of the AdmissionPolicy to delete.
     */
    public void deleteAdmissionPolicy(Long admissionPolicyId) {
        if (admissionPolicyId == null) {
            throw new RequestValidationException("admission policy id cannot be null.");
        }

        admissionPolicyRepository.deleteById(admissionPolicyId);
    }

}