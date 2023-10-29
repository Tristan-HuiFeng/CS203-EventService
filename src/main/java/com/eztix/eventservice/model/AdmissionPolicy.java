package com.eztix.eventservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@Entity(name = "admission_policy")
@Table(name = "ADMISSION_POLICY")
public class AdmissionPolicy {
    @Id
    @SequenceGenerator(name = "admission_policy_sequence", sequenceName = "admission_policy_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admission_policy_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "policy_order")
    private Short policyOrder;

    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "event_id")
    private Event event;

}