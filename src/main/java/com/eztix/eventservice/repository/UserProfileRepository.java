package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.UserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    @Query(
            value = "SELECT * from USER_PROFILE u_p WHERE u_p.active = TRUE",
            nativeQuery = true
    )
    Iterable<UserProfile> findAllByIsActive();

    @Query(
            value = "SELECT * from USER_PROFILE u_p WHERE u_p. is_employee = TRUE",
            nativeQuery = true
    )
    Iterable<UserProfile> findAllByIsEmployee();

    boolean existsUserProfileByEmail(String email);
}
