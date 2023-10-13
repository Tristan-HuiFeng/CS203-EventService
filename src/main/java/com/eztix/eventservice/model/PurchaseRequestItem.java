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
@Entity(name = "purchase_request_item")
@Table(name = "PURCHASE_REQUEST_ITEM")
public class PurchaseRequestItem {

    @Id
    @SequenceGenerator(name = "purchase_request_item_sequence", sequenceName = "purchase_request_item_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_request_item_sequence")
    @NotNull
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "quantity_request")
    private int quantityRequested;

    @NotNull
    @Column(name = "quantity_approved")
    private int quantityApproved;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "purchaseRequest_id")
    private PurchaseRequest purchaseRequest;

}

