package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Embeddable
public class PurchaseRequestItemId {

    @Id
    @SequenceGenerator(name = "purchaseRequestItem_sequence", sequenceName = "purchaseRequestItem_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchaseRequestItem_sequence")
    @Column(name = "purchaseRequestItem_id")
    @NotNull
    @Schema(hidden = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticketType_id")
    private TicketType ticketType;

    @ManyToOne
    @JoinColumn(name = "purchaseRequest_id")
    private PurchaseRequest purchaseRequest;
}
