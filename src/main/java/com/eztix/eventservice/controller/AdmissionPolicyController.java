package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.AdmissionPolicy;
import com.eztix.eventservice.service.AdmissionPolicyService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdmissionPolicyController {
    private final AdmissionPolicyService admissionPolicyService;

    public AdmissionPolicyController(AdmissionPolicyService admissionPolicyService){
        this.admissionPolicyService = admissionPolicyService;
    }

    @GetMapping("/api/v1/{eventId}/admission-policy")
    public ResponseEntity<List<AdmissionPolicy>>getAdmissionPolicyByEventId(@PathVariable Long eventId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(admissionPolicyService.getAllAdmissionPolicyByEventId(eventId));
    }

    @PostMapping("/api/v1/{eventId}/admission-policy")
    public ResponseEntity<AdmissionPolicy> addAdmissionPolicy(@PathVariable Long eventId, @RequestBody AdmissionPolicy admissionPolicy){
        return ResponseEntity.status(HttpStatus.CREATED).body(admissionPolicyService.addNewAdmissionPolicy(eventId, admissionPolicy));
    }

    @PutMapping("/api/v1/admission-policy/{id}")
    public ResponseEntity<AdmissionPolicy> updateAdmissionPolicy(@PathVariable Long admissionPolicyId, @RequestBody AdmissionPolicy admissionPolicy){
        admissionPolicy.setId(admissionPolicyId);
        return ResponseEntity.status(HttpStatus.OK).body(admissionPolicyService.updateAdmissionPolicy(admissionPolicy));
    }

    @DeleteMapping("/api/v1/admission-policy/{admissionPolicyId}")
    public ResponseEntity<String> deleteAdmissionPolicy(@PathVariable Long admissionPolicyId) {
        admissionPolicyService.deleteAdmissionPolicy(admissionPolicyId);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("admission policy with id %d has been deleted", admissionPolicyId));
    }
}