package com.alex.project.taskmanagerproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserLoginRequest {
    @NotNull
    @Email
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;

    public @NotNull @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Email @NotBlank String email) {
        this.email = email;
    }

    public @NotNull @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @NotBlank String password) {
        this.password = password;
    }
}
