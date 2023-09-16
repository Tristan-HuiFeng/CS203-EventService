package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Activity")
@Table(name = "ACTIVITY")
public class Activity {

    @Id
    @SequenceGenerator(name = "activity_sequence", sequenceName = "activity_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "start_datetime")
    private OffsetDateTime startDateTime;

    @NotNull
    @Column(name = "end_datetime")
    private OffsetDateTime  endDateTime;

    @NotNull
    @Column(name = "location")
    private String location;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy="activity",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<TicketType> ticketType;

}