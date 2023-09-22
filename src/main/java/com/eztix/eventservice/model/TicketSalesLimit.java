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
@Entity(name = "TicketSalesLimit")
@Table(name = "TICKET_SALES_LIMIT")
@IdClass(TicketSalesLimitId.class)
public class TicketSalesLimit {

    @Id
    @NotNull
    @Schema(hidden = true)
    private TicketSalesLimitId id;

    @NotNull
    @Column(name = "limit_vacancy")
    private int limitVacancy;

    @NotNull
    @Column(name = "occupied_vacancy")
    private int occupiedVacancy;

    @ManyToOne
    @JoinColumn(name = "salesRound_id")
    private SalesRound salesRound;

    @ManyToOne
    @JoinColumn(name = "ticketType_id")
    private TicketType ticketType;
}

