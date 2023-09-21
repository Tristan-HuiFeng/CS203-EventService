package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.*;
import java.util.List;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "SalesRound")
@Table(name = "SALES_ROUND")
public class SalesRound {

    @Id
    @SequenceGenerator(name = "salesRound_sequence", sequenceName = "salesRound_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salesRound_sequence")
    @Schema(hidden = true)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @NotNull
    @Column(name = "round_start")
    private OffsetDateTime roundStart;

    @NotNull
    @Column(name = "round_end")
    private OffsetDateTime roundEnd;

    @NotNull
    @Column(name = "purchase_start")
    private OffsetDateTime purchaseStart;

    @NotNull
    @Column(name = "purchase_end")
    private OffsetDateTime purchaseEnd;

    @NotNull
    @Column(name = "sales_type")
    private String salesType;

    @OneToMany(mappedBy = "salesRound", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TicketSalesLimit> ticketSalesLimits;

}
