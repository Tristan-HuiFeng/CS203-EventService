package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.AdmissionPolicy;
import com.eztix.eventservice.service.AdmissionPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AdmissionPolicyController {

    private final AdmissionPolicyService admissionPolicyService;

    /**
     * Retrieve admission policies based on eventId.
     * 
     * @param eventId a long value representing the unique identifier of the Event of the AdmissionPolicy to retrieve.
     * @return a ResponseEntity containing the list of retrieved AdmissionPolicy and an OK status.
     */
    @CrossOrigin
    @GetMapping("/api/v1/event/{eventId}/admission-policy")
    public ResponseEntity<List<AdmissionPolicy>> getAdmissionPolicyByEventId(@PathVariable Long eventId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(admissionPolicyService.getAllAdmissionPolicyByEventId(eventId));
    }

//    @PostMapping("/api/v1/{eventId}/admission-policy")
//    public ResponseEntity<AdmissionPolicy> addAdmissionPolicy(@PathVariable Long eventId, @RequestBody AdmissionPolicy admissionPolicy){
//        return ResponseEntity.status(HttpStatus.CREATED).body(admissionPolicyService.addNewAdmissionPolicy(eventId, admissionPolicy));
//    }

    /**
     * Update the info of an admission policy of an event.
     * 
     * @param admissionPolicyId a long value representing the unique identifier of the AdmissionPolicy to update.
     * @param admissionPolicy an AdmissionPolicy object containing the new AdmissionPolicy info to be updated.
     * @return a ResponseEntity containing the updated AdmissionPolicy and an OK status.
     */
    @PutMapping("/api/v1/admission-policy/{id}")
    public ResponseEntity<AdmissionPolicy> updateAdmissionPolicy(@PathVariable Long admissionPolicyId, @RequestBody AdmissionPolicy admissionPolicy){
        admissionPolicy.setId(admissionPolicyId);
        return ResponseEntity.status(HttpStatus.OK).body(admissionPolicyService.updateAdmissionPolicy(admissionPolicy));
    }

    /**
     * Delete an admission policy.
     * 
     * @param admissionPolicyId a long value representing the unique identifier of the AdmissionPolicy to delete.
     * @return a ResponseEntity containing a string that states the admission policy deleted and its unique identifier and an OK status.
     */
    @DeleteMapping("/api/v1/admission-policy/{admissionPolicyId}")
    public ResponseEntity<String> deleteAdmissionPolicy(@PathVariable Long admissionPolicyId) {
        admissionPolicyService.deleteAdmissionPolicy(admissionPolicyId);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("admission policy with id %d has been deleted", admissionPolicyId));
    }
}