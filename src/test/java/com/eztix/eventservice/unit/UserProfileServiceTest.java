package com.eztix.eventservice.unit;

import com.eztix.eventservice.dto.PasswordDTO;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.UserProfile;
import com.eztix.eventservice.repository.UserProfileRepository;
import com.eztix.eventservice.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.server.UID;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
 class UserProfileServiceTest {
    @Mock
    private UserProfileRepository userProfileRepository;
    @InjectMocks
    private UserProfileService testUserProfileService;

    @Test
    void givenNewUserProfile_whenAddUserProfile_andNoDuplicateEmail_thenSuccess(){
        //given
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setFirst_name("John");
        userProfile.setLast_name("Cando");
        userProfile.setEmail("JohnCando@smu.edu.sg");
        userProfile.setPassword("password");
        userProfile.setIs_employee(true);
        userProfile.setBlocked(false);
        userProfile.setActive(true);
        userProfile.setCountry_residence("Singapore");
        userProfile.setBirthday(LocalDate.now());

        //when
        testUserProfileService.addNewUserProfile(userProfile);

        //then
        ArgumentCaptor<UserProfile> userProfileArgumentCaptor =
                ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(userProfileArgumentCaptor.capture());

        UserProfile capturedUserProfile = userProfileArgumentCaptor.getValue();

        assertThat(capturedUserProfile).isEqualTo(userProfile);
    }

    @Test
    void givenNewUserProfile_whenAddUserProfile_andDuplicateEmail_thenFail(){
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setFirst_name("John");
        userProfile.setLast_name("Cando");
        userProfile.setEmail("JohnCando@smu.edu.sg");
        userProfile.setPassword("password");
        userProfile.setIs_employee(true);
        userProfile.setBlocked(false);
        userProfile.setActive(true);
        userProfile.setCountry_residence("Singapore");
        userProfile.setBirthday(LocalDate.now());

        given(userProfileRepository.existsUserProfileByEmail(userProfile.getEmail()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> testUserProfileService.addNewUserProfile(userProfile))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("email " + userProfile.getEmail() + "taken");

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    void givenIdNotInDB_whenRetrieveById_throwResourceNotFoundException(){
        //given
        given(userProfileRepository.findById(1L)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> testUserProfileService.getUserProfileById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("user with id %d does not exist.", 1L);

    }

    @Test
    void givenUserProfileExist_whenRetrieve_thenSuccessful(){
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setFirst_name("John");
        userProfile.setLast_name("Cando");
        userProfile.setEmail("JohnCando@smu.edu.sg");
        userProfile.setPassword("password");
        userProfile.setIs_employee(true);
        userProfile.setBlocked(false);
        userProfile.setActive(true);
        userProfile.setCountry_residence("Singapore");
        userProfile.setBirthday(LocalDate.now());

        given(userProfileRepository.findById(userProfile.getId())).willReturn(Optional.of(userProfile));

        // when
        UserProfile retrieved = testUserProfileService.getUserProfileById(userProfile.getId());
        // then
        assertThat(retrieved).isEqualTo(userProfile);
    }

    @Test
    void getAllUserProfile(){
        testUserProfileService.getAllUserProfile();
        verify(userProfileRepository).findAll();
    }

    @Test
    void getAllActiveUserProfile(){
        testUserProfileService.getAllActiveUserProfile(true);
        verify(userProfileRepository).findAllByIsActive();
    }

    @Test
    void getAllEmployee(){
        testUserProfileService.getAllEmployees((true));
        verify(userProfileRepository).findAllByIsEmployee();
    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException(){
        //given
        UserProfile userProfile = new UserProfile();
        userProfile.setFirst_name("John");
        userProfile.setLast_name("Cando");
        userProfile.setEmail("JohnCando@smu.edu.sg");
        userProfile.setPassword("password");
        userProfile.setIs_employee(true);
        userProfile.setBlocked(false);
        userProfile.setActive(true);
        userProfile.setCountry_residence("Singapore");
        userProfile.setBirthday(LocalDate.now());
        // when
        // then
        assertThatThrownBy(() -> testUserProfileService.updateUserProfile(userProfile))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("profile id cannot be null.");
    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException(){
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setFirst_name("John");
        userProfile.setLast_name("Cando");
        userProfile.setEmail("JohnCando@smu.edu.sg");
        userProfile.setPassword("password");
        userProfile.setIs_employee(true);
        userProfile.setBlocked(false);
        userProfile.setActive(true);
        userProfile.setCountry_residence("Singapore");
        userProfile.setBirthday(LocalDate.now());

        given(userProfileRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testUserProfileService.updateUserProfile(userProfile))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("profile with id %d does not exist", userProfile.getId()));
    }

    @Test
    void givenInvalidUserOldPassword_whenUpdate_throwRequestValidationException(){
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setFirst_name("John");
        userProfile.setLast_name("Cando");
        userProfile.setEmail("JohnCando@smu.edu.sg");
        userProfile.setPassword("password");
        userProfile.setIs_employee(true);
        userProfile.setBlocked(false);
        userProfile.setActive(true);
        userProfile.setCountry_residence("Singapore");
        userProfile.setBirthday(LocalDate.now());
        given(userProfileRepository.findById(userProfile.getId())).willReturn(Optional.of(userProfile));
        // when
        String oldPassword = "new jeans";
        String newPassword = "hello world";
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setOldPassword(oldPassword);
        passwordDTO.setNewPassword(newPassword);
        passwordDTO.setUserId(userProfile.getId());
        // then
        assertThatThrownBy(()->testUserProfileService.updatePassword(passwordDTO))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("invalid old password");

    }

    @Test
    void givenInvalidPasswordLength_whenUpdatePassword_throwRequestValidationException(){
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setFirst_name("John");
        userProfile.setLast_name("Cando");
        userProfile.setEmail("JohnCando@smu.edu.sg");
        userProfile.setPassword("password");
        userProfile.setIs_employee(true);
        userProfile.setBlocked(false);
        userProfile.setActive(true);
        userProfile.setCountry_residence("Singapore");
        userProfile.setBirthday(LocalDate.now());
        // when
        String oldPassword = userProfile.getPassword();
        String newPassword = "hello";

        PasswordDTO passwordDTO= new PasswordDTO();
        passwordDTO.setNewPassword(newPassword);
        passwordDTO.setOldPassword(oldPassword);
        passwordDTO.setUserId(userProfile.getId());
        // then
        assertThatThrownBy(() -> testUserProfileService.updatePassword(passwordDTO)).
                isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("password must be at least 8 characters long");

    }

}
