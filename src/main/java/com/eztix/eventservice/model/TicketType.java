    package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="TicketType")
@Table(name= "TICKETTYPE")
public class TicketType {

    @Id
    @SequenceGenerator(name = "ticketType_sequence", sequenceName = "ticketType_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketType_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "activity_id")
    private Long activityId;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "price")
    private double price;

    @NotNull
    @Column(name = "total_vacancy")
    private int total_vacancy;

    @NotNull
    @Column(name = "occupied_count")
    private int occupied_count;

    @NotNull
    @Column(name = "reserved_count")
    private int reserved_count;

    @NotNull
    @Column(name = "description")
    private String description;
    
}
