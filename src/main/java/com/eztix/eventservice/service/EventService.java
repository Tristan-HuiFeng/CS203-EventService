package com.eztix.eventservice.service;

import com.eztix.eventservice.dto.request.NewEvent;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.*;
import com.eztix.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AdmissionPolicyService admissionPolicyService;
    private final ActivityService activityService;
    private final TicketTypeService ticketTypeService;

    public Event addNewEvent(NewEvent newEvent) {
        Event inputEvent = Event.builder()
                .name(newEvent.getName())
                .category(newEvent.getCategory())
                .artist(newEvent.getArtist())
                .description(newEvent.getDescription())
                .bannerURL(newEvent.getBannerURL())
                .seatMapURL(newEvent.getSeatMapURL())
                .location(newEvent.getLocation())
                .isFeatured(newEvent.getIsFeatured())
                .featureSequence(newEvent.getFeatureSequence() == null ? -1 : newEvent.getFeatureSequence())
                .start_datetime(newEvent.getStart_datetime())
                .end_datetime(newEvent.getEnd_datetime())
                .build();
        Event savedEvent = eventRepository.save(inputEvent);

        AtomicInteger i = new AtomicInteger();
        newEvent.getAdmissionPolicies().forEach((policy) -> {
            i.getAndIncrement();
            AdmissionPolicy inputAP = AdmissionPolicy.builder()
                    .description(policy.getDescription())
                    .name(policy.getName())
                    .policyOrder(i.shortValue())
                    .build();
            admissionPolicyService.addNewAdmissionPolicy(savedEvent, inputAP);
        });

        newEvent.getActivities().forEach((activity) -> {
            Activity inputActivity = Activity.builder()
                    .startDateTime(activity.getStartDateTime())
                    .endDateTime(activity.getEndDateTime())
                    .build();
            Activity savedActivity = activityService.addNewActivity(savedEvent, inputActivity);

            activity.getTicketTypes().forEach((ticketType -> {
                TicketType inputTicketType = TicketType.builder()
                        .type(ticketType.getType())
                        .price(ticketType.getPrice())
                        .totalVacancy(ticketType.getTotalVacancy())
                        .occupiedCount(ticketType.getOccupiedCount())
                        .reservedCount(ticketType.getReservedCount())
                        .build();
                ticketTypeService.addNewTicketType(savedActivity, inputTicketType);
            }));
        });

        return savedEvent;
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("event with id %d does not exist.", id))
        );
    }

    @Transactional
    public Event updateEvent(Event event) {
        if (event == null || event.getId() == null) {
            throw new RequestValidationException("event id cannot be null.");
        }

        eventRepository.findById(event.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("event with id %d does not exist.", event.getId()))
        );

        return eventRepository.save(event);
    }

    public Iterable<Event> getAllEvents(boolean featuredOnly) {
        return featuredOnly ? eventRepository.findAllByIsFeaturedTrueOrderByFeatureSequence() :
                eventRepository.findAll();
    }

    public void deleteAll() {
        eventRepository.deleteAll();
    }

}
