package com.eztix.eventservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Entity(name = "activity")
@Table(name = "ACTIVITY")
public class Activity {

    @Id
    @SequenceGenerator(name = "activity_sequence", sequenceName = "activity_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "start_datetime")
    private OffsetDateTime startDateTime;

    @NotNull
    @Column(name = "end_datetime")
    private OffsetDateTime  endDateTime;

    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "event_id")
    private Event event;

    @JsonManagedReference
    @OneToMany(mappedBy="activity",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<TicketType> ticketTypes;

}