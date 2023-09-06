package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;

@Entity
public class Activity {

    @Id
    @SequenceGenerator(name = "activity_sequence", sequenceName = "activity_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_sequence")
    @Schema(hidden = true)
    private Long id;

    
    
}
