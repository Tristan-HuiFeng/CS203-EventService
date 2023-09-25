package com.eztix.eventservice.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Entity
@Table(name = "TICKET_SALES_LIMIT")
public class TicketSalesLimit {

    @Id
    @SequenceGenerator(name = "ticket_sales_limit_sequence", sequenceName = "ticket_sales_limit_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_sales_limit_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "limit_vacancy")
    private int limitVacancy;

    @NotNull
    @Column(name = "occupied_vacancy")
    private int occupiedVacancy;

    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "ticketType_id")
    private TicketType ticketType;

    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "salesRound_id")
    private SalesRound salesRound;

}

