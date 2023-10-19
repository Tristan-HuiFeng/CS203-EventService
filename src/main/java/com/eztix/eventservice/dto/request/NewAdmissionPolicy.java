package com.eztix.eventservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class NewAdmissionPolicy {
    @NotNull
    private String description;

    @NotNull
    private String name;

}
