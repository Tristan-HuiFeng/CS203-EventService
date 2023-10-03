package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_PROFILE")
@Entity(name = "user_profile")
public class UserProfile {

    @Id
    @SequenceGenerator(name = "user_profile_sequence", sequenceName = "user_profile_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_profile_sequence")
    @Schema(hidden = true)
    Long id;

    @NotNull
    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Size(min = 8)
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "is_employee")
    private Boolean is_employee;

    @NotNull
    @Column(name = "blocked")
    private Boolean blocked;

    @NotNull
    @Column(name = "active")
    private Boolean active;

    @NotNull
    @Column(name = "contact_number")
    private String contact_number;

    @NotNull
    @Column(name = "postal_code")
    private String postal_code;

    @NotNull
    @Column(name = "country_residence")
    private String country_residence;

    @NotNull
    @Column(name = "birthday")
    private OffsetDateTime birthday;

    @NotNull
    @Column(name = "payment_method")
    private String payment;

}
