package com.eztix.eventservice.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.AdmissionPolicy;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.AdmissionPolicyRepository;
import com.eztix.eventservice.service.AdmissionPolicyService;
import com.eztix.eventservice.service.EventService;

@ExtendWith(MockitoExtension.class)
public class AdmissionPolicyServiceTest {
    @Mock
    private AdmissionPolicyRepository admissionPolicyRepository;
    @InjectMocks
    private AdmissionPolicyService testAdmissionPolicyService;
    @Mock
    private EventService eventService;
    private static Event event;

    @BeforeAll
    static void setup() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("urk1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);
    }

    @Test
    void givenNewAdmissionPolicy_whenAddAdmissionPolicy_thenSuccess() {
        // given

        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setId(1L);
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        admissionPolicy.setPolicyOrder((short) 1);

        // when
        testAdmissionPolicyService.addNewAdmissionPolicy(event, admissionPolicy);

        // then
        ArgumentCaptor<AdmissionPolicy> admissionPolicyArgumentCaptor = ArgumentCaptor.forClass(AdmissionPolicy.class);

        verify(admissionPolicyRepository).save(admissionPolicyArgumentCaptor.capture());

        AdmissionPolicy capturedAdmissionPolicy = admissionPolicyArgumentCaptor.getValue();

        assertThat(capturedAdmissionPolicy).isEqualTo(admissionPolicy);

    }

    @Test
    void givenIdNotInDB_whenRetrieveById_throwResourcesNotFoundException() {
        // given
        // when
        // then
        assertThatThrownBy(() -> testAdmissionPolicyService.getAllAdmissionPolicyByEventId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("event with id %d does not have admission policy", 1L);
    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given

        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        Short s = 2;
        admissionPolicy.setPolicyOrder(s);

        // when
        // then
        assertThatThrownBy(() -> testAdmissionPolicyService.updateAdmissionPolicy(admissionPolicy))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("admission policy id cannot be null.");
    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException() {
        // given

        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setId(1L);
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        Short s = 2;
        admissionPolicy.setPolicyOrder(s);

        given(admissionPolicyRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testAdmissionPolicyService.updateAdmissionPolicy(admissionPolicy))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        String.format("admission policy with id %d does not exist", admissionPolicy.getId()));

    }

    @Test
    void getAllAdmissionPolicy() {
        // given: empty list of admission policies
        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setId(1L);
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        Short s = 2;
        admissionPolicy.setPolicyOrder(s);

        // when
        // then
        assertThatThrownBy(() -> testAdmissionPolicyService.updateAdmissionPolicy(admissionPolicy))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        String.format("admission policy with id %d does not exist", admissionPolicy.getId()));
    }

    @Test
    void givenNullId_whenDelete_throwRequestValidationException() {
        // given
        AdmissionPolicy admissionPolicy = new AdmissionPolicy();
        admissionPolicy.setEvent(event);
        admissionPolicy.setDescription("This is a test policy");
        Short s = 2;
        admissionPolicy.setPolicyOrder(s);

        // when
        // then
        assertThatThrownBy(() -> testAdmissionPolicyService.deleteAdmissionPolicy(admissionPolicy.getId()))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("admission policy id cannot be null.");

    }

}
