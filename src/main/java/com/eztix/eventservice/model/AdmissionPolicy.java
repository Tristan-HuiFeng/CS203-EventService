package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@Entity(name = "Admission Policy")
@Table(name = "ADMISSION_POLICY")
public class AdmissionPolicy {
    @Id
    @SequenceGenerator(name = "admission_policy_sequence", sequenceName = "admission_policy_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admission_policy_sequence")
    @Schema(hidden = true)
    private Long id;

}
