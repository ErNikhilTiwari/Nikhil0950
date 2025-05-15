package com.cg.siptracker.dto;
import com.cg.siptracker.model.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, message = "Password must be at least Six characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,}$", message = "New Password must have at least 6 characters, 1 uppercase letter and 1 digit")
    private String password;

    @NotNull(message = "Role is mandatory")
    private Role role;
}
