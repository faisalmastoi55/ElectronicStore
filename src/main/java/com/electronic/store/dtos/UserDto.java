package com.electronic.store.dtos;

import com.electronic.store.entities.Role;
import com.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min = 3, max = 15, message = "Invalid Name !!")
    @NotBlank(message = "Name is required")
    private String name;

//    @Email(message = "Invalid user email !!")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid User Email !!")
    @NotBlank(message = "Email is required !!")
    private String email;

    @NotBlank(message = "Password is required !!")
    private String password;

    @NotBlank(message = "Gender is required !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;


    @ImageNameValid
    private String imageName;

    private Set<RoleDto> roles = new HashSet<>();
}
