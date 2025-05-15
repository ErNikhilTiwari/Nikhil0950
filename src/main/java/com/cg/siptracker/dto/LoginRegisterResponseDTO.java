package com.cg.siptracker.dto;

import com.cg.siptracker.model.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRegisterResponseDTO {
    private String fullName;
    private String email;
    private String token;
    private Role role;

    public LoginRegisterResponseDTO(String fullName, String email, String token) {
        this.fullName = fullName;
        this.email = email;
        this.token = token;
    }

    public LoginRegisterResponseDTO(String fullName, String email, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
}
