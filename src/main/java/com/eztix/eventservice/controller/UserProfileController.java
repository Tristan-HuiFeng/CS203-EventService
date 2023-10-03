package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.UserProfile;
import com.eztix.eventservice.service.UserProfileService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.h2.engine.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserProfileController {
    public final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }
    @GetMapping("/api/v1/userprofile/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).
                body(userProfileService.getUserProfileById(id));
    }
    @GetMapping("api/v1/userprofile")
    public ResponseEntity<Iterable<UserProfile>> getAllUserProfile(){
        return ResponseEntity.status(HttpStatus.OK).
                body(userProfileService.getAllUserProfile());
    }

    @GetMapping("/api/v1/userprofile/active")
    public ResponseEntity<Iterable<UserProfile>> getAllActiveUserProfile(@RequestParam(required = false, defaultValue = "false") boolean activeOnly){
        return ResponseEntity.status(HttpStatus.OK).
                body(userProfileService.getAllActiveUserProfile(activeOnly));
    }

    @GetMapping("/api/v1/userprofile/employees")
    public ResponseEntity<Iterable<UserProfile>> getAllEmployees(@RequestParam(required = false, defaultValue = "false") boolean employeeOnly){
        return ResponseEntity.status(HttpStatus.OK).
                body(userProfileService.getAllEmployees(employeeOnly));
    }

    @PutMapping("/api/v1/userprofile/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long id, @RequestBody UserProfile userProfile){
        return ResponseEntity.status(HttpStatus.OK).
                body(userProfileService.updateUserProfile(userProfile));
    }

    // update user password
    @PutMapping("/api/v1/userprofile/{id}/password")
    public ResponseEntity<String> updatePassword(@Valid @PathVariable Long id, @RequestParam("old password") String oldPassword,
                                                 @RequestParam("new password") String newPassword){
        return ResponseEntity.status(HttpStatus.OK).
                body(userProfileService.updatePassword(id, oldPassword, newPassword));

    }

    @PostMapping("/api/v1/userprofile/register")
    public ResponseEntity<UserProfile> addNewUserProfile(@RequestBody UserProfile userProfile){
        return ResponseEntity.status(HttpStatus.CREATED).
                body(userProfileService.addNewUserProfile(userProfile));
    }






}
