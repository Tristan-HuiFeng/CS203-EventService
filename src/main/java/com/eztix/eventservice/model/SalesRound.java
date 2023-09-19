package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.*;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="SalesRound")
@Table(name= "SALES_ROUND")
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
    private OffsetDateTime round_start;

    @NotNull
    @Column(name = "round_end")
    private OffsetDateTime round_end;

    @NotNull
    @Column(name = "purchase_start")
    private OffsetDateTime purchase_start;

    @NotNull
    @Column(name = "purchase_end")
    private OffsetDateTime purchase_end;

    @NotNull
    @Column(name = "sales_type")
    private String sales_type;
    
}
