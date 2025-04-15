package com.alex.project.taskmanagerproject.dto;

import jakarta.validation.constraints.*;

public class UserRegistrationRequest {

    @NotNull
    @NotBlank
    private String nickname;

    @NotNull
    @Email
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;

    public @NotNull @NotBlank String getNickname() {
        return nickname;
    }

    public void setNickname(@NotNull @NotBlank String nickname) {
        this.nickname = nickname;
    }

    public @NotNull @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Email @NotBlank String email) {
        this.email = email;
    }

    public @NotNull @NotBlank @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @NotBlank @Size(min = 8) String password) {
        this.password = password;
    }
}
