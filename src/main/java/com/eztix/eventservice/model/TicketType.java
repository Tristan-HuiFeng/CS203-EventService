    package com.eztix.eventservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;

@Entity
public class TicketType {

    @Id
    @SequenceGenerator(name = "ticketType_sequence", sequenceName = "ticketType_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketType_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "activity_id")
    private Long activityId;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "price")
    private double price;

    @NotNull
    @Column(name = "total_vacancy")
    private int total_vacancy;

    @NotNull
    @Column(name = "occupied_count")
    private int occupied_count;

    @NotNull
    @Column(name = "reserved_count")
    private int reserved_count;

    @NotNull
    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotal_vacancy() {
        return total_vacancy;
    }

    public void setTotal_vacancy(int total_vacancy) {
        this.total_vacancy = total_vacancy;
    }

    public int getOccupied_count() {
        return occupied_count;
    }

    public void setOccupied_count(int occupied_count) {
        this.occupied_count = occupied_count;
    }

    public int getReserved_count() {
        return reserved_count;
    }

    public void setReserved_count(int reserved_count) {
        this.reserved_count = reserved_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
