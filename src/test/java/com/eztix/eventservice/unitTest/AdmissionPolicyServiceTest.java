package com.eztix.eventservice.unitTest;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.AdmissionPolicy;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.AdmissionPolicyRepository;
import com.eztix.eventservice.service.AdmissionPolicyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AdmissionPolicyServiceTest {
    @Mock
    private AdmissionPolicyRepository admissionPolicyRepository;
    @InjectMocks
    private AdmissionPolicyService testAdmissionPolicyService;

    @Test
    void givenNewAdmissionPolicy_whenAddAdmissionPolicy_thenSuccess(){
        // given
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("urk1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);

        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setId(1L);
        admissionPolicy.setName("Test admission policy");
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        admissionPolicy.setPolicyOrder("This is a test policy");

        // when
        testAdmissionPolicyService.addNewAdmissionPolicy(admissionPolicy);

        // then
        ArgumentCaptor<AdmissionPolicy> admissionPolicyArgumentCaptor =
                ArgumentCaptor.forClass(AdmissionPolicy.class);

        verify(admissionPolicyRepository).save(admissionPolicyArgumentCaptor.capture());

        AdmissionPolicy capturedAdmissionPolicy = admissionPolicyArgumentCaptor.getValue();

        assertThat(capturedAdmissionPolicy).isEqualTo(admissionPolicy);

    }

    @Test
    void givenIdNotInDB_whenRetrieveById_throwResourcesNotFoundException() {
        // given
        given(admissionPolicyRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testAdmissionPolicyService.getAdmissionPolicyById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("admission policy with id %d does not exist", 1L);
    }

    @Test
    void givenAdmissionPolicyExist_whenRetrieve_thenSuccessful(){
        // given
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("urk1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);

        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setId(1L);
        admissionPolicy.setName("Test admission policy");
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        admissionPolicy.setPolicyOrder("This is a test policy");

        // when

        given(admissionPolicyRepository.findById(admissionPolicy.getId())).willReturn(Optional.of(admissionPolicy));

        AdmissionPolicy retrievedAdmissionPolicy = testAdmissionPolicyService.getAdmissionPolicyById(admissionPolicy.getId());
        // then
        assertThat(retrievedAdmissionPolicy).isEqualTo(admissionPolicy);
    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException(){
        // given
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("urk1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);

        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setName("Test admission policy");
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        admissionPolicy.setPolicyOrder("This is a test policy");

        // when
        // then
        assertThatThrownBy(() -> testAdmissionPolicyService.updateAdmissionPolicy(admissionPolicy))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("admission policy id cannot be null.");
    }
    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException(){
        // given
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("urk1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);

        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setId(1L);
        admissionPolicy.setName("Test admission policy");
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        admissionPolicy.setPolicyOrder("This is a test policy");

        given(admissionPolicyRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testAdmissionPolicyService.updateAdmissionPolicy(admissionPolicy))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("admission policy with id %d does not exist", admissionPolicy.getId()));

    }

    @Test
    void getAllAdmissionPolicy(){
        testAdmissionPolicyService.getAllAdmissionPolicy();
        verify(admissionPolicyRepository).findAll();
    }


}
