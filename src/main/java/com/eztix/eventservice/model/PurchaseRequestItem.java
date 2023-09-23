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
@Entity(name = "PurchaseRequestItem")
@Table(name = "PURCHASE_REQUEST_ITEM")
@IdClass(PurchaseRequestItemId.class)
public class PurchaseRequestItem {

    @Id
    @NotNull
    @Schema(hidden = true)
    private PurchaseRequestItemId id;

    @NotNull
    @Column(name = "quantity_requestd")
    private int quantityRequested;

    @NotNull
    @Column(name = "quantity_approved")
    private int quanitityApproved;

    @ManyToOne
    @JoinColumn(name = "ticketType_id")
    private TicketType ticketType;

    @ManyToOne
    @JoinColumn(name = "purchaseRequest_id")
    private PurchaseRequest purchaseRequest;
}

