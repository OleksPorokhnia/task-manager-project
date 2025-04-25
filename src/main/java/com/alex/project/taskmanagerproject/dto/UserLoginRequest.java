package com.alex.project.taskmanagerproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserLoginRequest {
    @NotNull(message = "Email cannot be blank")
    @Email(message = "Incorrect email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Password cannot be blank")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    public UserLoginRequest() {
    }

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public @NotNull(message = "Email cannot be blank") @Email(message = "Incorrect email format") @NotBlank(message = "Email cannot be blank") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email cannot be blank") @Email(message = "Incorrect email format") @NotBlank(message = "Email cannot be blank") String email) {
        this.email = email;
    }

    public @NotNull(message = "Password cannot be blank") @NotBlank(message = "Password cannot be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password cannot be blank") @NotBlank(message = "Password cannot be blank") String password) {
        this.password = password;
    }
}
