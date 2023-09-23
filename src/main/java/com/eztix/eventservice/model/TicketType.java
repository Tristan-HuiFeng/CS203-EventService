    package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="TicketType")
@Table(name= "TICKET_TYPE")
public class TicketType {

    @Id
    @SequenceGenerator(name = "ticketType_sequence", sequenceName = "ticketType_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketType_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "price")
    private double price;

    @NotNull
    @Column(name = "total_vacancy")
    private int totalVacancy;

    @NotNull
    @Column(name = "occupied_count")
    private int occupiedCount;

    @NotNull
    @Column(name = "reserved_count")
    private int reservedCount;

    @NotNull
    @Column(name = "description")
    private String description;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "activity_id")
    private Activity activity;

}
