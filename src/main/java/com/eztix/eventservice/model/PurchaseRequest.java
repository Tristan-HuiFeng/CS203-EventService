package com.eztix.eventservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder
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

    @Column(name = "queue_number")
    private Long queueNumber;

    @NotNull
    @Column(name = "customer_id")
    private String customerId;

    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "salesRound_id")
    private SalesRound salesRound;

    @JsonManagedReference
    @OneToMany(mappedBy="purchaseRequest",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<PurchaseRequestItem> purchaseRequestItems;

}
