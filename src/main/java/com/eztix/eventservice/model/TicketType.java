package com.eztix.eventservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


    @Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "TICKET_TYPE")
public class TicketType {

    @Id
    @SequenceGenerator(name = "ticket_type_sequence", sequenceName = "ticket_type_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_type_sequence")
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

    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @JsonManagedReference
    @OneToMany(mappedBy="ticketType", fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<TicketSalesLimit> ticketSalesLimits;


}
