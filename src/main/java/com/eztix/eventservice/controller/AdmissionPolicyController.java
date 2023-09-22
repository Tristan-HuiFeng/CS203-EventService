package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.AdmissionPolicy;
import com.eztix.eventservice.service.AdmissionPolicyService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdmissionPolicyController {
    private final AdmissionPolicyService admissionPolicyService;

    public AdmissionPolicyController(AdmissionPolicyService admissionPolicyService){
        this.admissionPolicyService = admissionPolicyService;
    }

    @GetMapping("admissionPolicy/{id}")
    public ResponseEntity<AdmissionPolicy>getAdmissionPolicyById(@PathVariable Long iD){
        return ResponseEntity.status(HttpStatus.OK)
                .body(admissionPolicyService.getAdmissionPolicyById(iD));
    }

    @GetMapping("admissionPolicy/all")
    public ResponseEntity<Iterable<AdmissionPolicy>> getAllAdmissionPolicy(){
        return ResponseEntity.status(HttpStatus.OK).body(admissionPolicyService.getAllAdmissionPolicy());
    }

    @PostMapping("admissionPolicy/add")
    public ResponseEntity<AdmissionPolicy> addAdmissionPolicy(@RequestBody AdmissionPolicy admissionPolicy){
        return ResponseEntity.status(HttpStatus.CREATED).body(admissionPolicyService.addNewAdmissionPolicy(admissionPolicy));
    }

    @PutMapping("admissionPolicy/{id}")
    public ResponseEntity<AdmissionPolicy> updateAdmissionPolicy(@PathVariable Long iD, @RequestBody AdmissionPolicy admissionPolicy){
        admissionPolicy.setId(iD);
        return ResponseEntity.status(HttpStatus.OK).body(admissionPolicyService.updateAdmissionPolicy(admissionPolicy));
    }
}