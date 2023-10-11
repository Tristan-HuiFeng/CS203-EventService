package com.eztix.eventservice.service;

import com.eztix.eventservice.controller.UserProfileController;
import com.eztix.eventservice.dto.PasswordDTO;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.UserProfile;
import com.eztix.eventservice.repository.UserProfileRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository){
        this.userProfileRepository = userProfileRepository;
    }
    public UserProfile getUserProfileById(Long id) {
        return userProfileRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("user with id %d does not exist.", id)));
    }

    public Iterable<UserProfile> getAllUserProfile(){
        return userProfileRepository.findAll();
    }

    public Iterable<UserProfile> getAllActiveUserProfile(boolean activeOnly){
        if (activeOnly){
            return userProfileRepository.findAllByIsActive();
        }
        else{
            throw new ResourceNotFoundException("no active users found");
        }
    }

    public Iterable<UserProfile> getAllEmployees(boolean employeeOnly){
        if (employeeOnly){
            return userProfileRepository.findAllByIsEmployee();
        }
        else{
            throw new ResourceNotFoundException("no employees found");
        }
    }

    public UserProfile addNewUserProfile(UserProfile userProfile){
        boolean existsEmail = userProfileRepository
                .existsUserProfileByEmail(userProfile.getEmail());

        if (existsEmail){
            throw new RequestValidationException(
                    "email " + userProfile.getEmail() + "taken"
            );
        }
        return userProfileRepository.save(userProfile);
    }

    @Transactional
    public UserProfile updateUserProfile(UserProfile userProfile){
        if (userProfile == null || userProfile.getId() == null){
            throw new RequestValidationException("profile id cannot be null.");
        }
        userProfileRepository.findById((userProfile.getId())).orElseThrow(() ->
                new ResourceNotFoundException(String.format("profile with id %d does not exist", userProfile.getId())));
        return userProfileRepository.save(userProfile);
    }
    public boolean oldPasswordIsValid(@NotNull UserProfile user, String oldPassword){
        return user.getPassword().equals(oldPassword);
    }

    @Transactional
    public String updatePassword(PasswordDTO passwordDTO){
        String oldPassword = passwordDTO.getOldPassword();
        String newPassword = passwordDTO.getNewPassword();
        Long id = passwordDTO.getUserId();
        if (oldPassword.length() < 8 || newPassword.length() < 8){
            throw new RequestValidationException("password must be at least 8 characters long");
        }
        Optional<UserProfile> user = userProfileRepository.findById(id);
        if (user.isPresent()){
            UserProfile current = user.get();
            if(oldPasswordIsValid(current,oldPassword)){
                current.setPassword(newPassword);
                userProfileRepository.save(current);
                return "successful password change";
            }
            else{
                throw new RequestValidationException("invalid old password");
            }
        }
        else{
            throw new ResourceNotFoundException(String.format("user with id %d does not exist", id));
        }
    }
}
