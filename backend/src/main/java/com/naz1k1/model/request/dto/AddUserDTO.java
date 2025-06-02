package com.naz1k1.model.request.dto;

import lombok.Data;

@Data
public class AddUserDTO {
    private String username;
    private String password;
    private String email;
}
