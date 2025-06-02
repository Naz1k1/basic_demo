package com.naz1k1.model.request.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private Integer status;
}
