package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "purchase_request")
@Table(name = "PURCHASE_REQUEST")
public class PurchaseRequest {

    @Id
    @SequenceGenerator(name = "purchaseRequest_sequence", sequenceName = "purchaseRequest_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchaseRequest_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "status")
    private String status;

    @NotNull
    @Column(name = "queue_number")
    private Long queueNumber;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "salesRound_id")
    private SalesRound salesRound;

    @NotNull
    @Column(name = "customer_id")
    private String customer;

}
