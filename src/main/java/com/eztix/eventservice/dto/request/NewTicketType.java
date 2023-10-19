package com.eztix.eventservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class NewTicketType {
    @NotNull
    private String type;

    @NotNull
    private double price;

    @NotNull
    private int totalVacancy;

    @NotNull
    private int occupiedCount;

    @NotNull
    private int reservedCount;

}
