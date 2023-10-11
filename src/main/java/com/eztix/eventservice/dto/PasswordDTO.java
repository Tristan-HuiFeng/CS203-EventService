package com.eztix.eventservice.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    private String oldPassword;
    private String newPassword;
    private Long userId;
}
