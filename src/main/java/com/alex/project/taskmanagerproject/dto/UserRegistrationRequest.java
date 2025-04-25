package com.alex.project.taskmanagerproject.dto;

import jakarta.validation.constraints.*;

public class UserRegistrationRequest {

    @NotNull(message = "Nickname cannot be blank")
    @NotBlank(message = "Nickname cannot be blank")
    private String nickname;

    @NotNull(message = "Email cannot be blank")
    @Email(message = "Incorrect email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Password cannot be blank")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8)
    private String password;

    public UserRegistrationRequest(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public @NotNull(message = "Nickname cannot be blank") @NotBlank(message = "Nickname cannot be blank") String getNickname() {
        return nickname;
    }

    public void setNickname(@NotNull(message = "Nickname cannot be blank") @NotBlank(message = "Nickname cannot be blank") String nickname) {
        this.nickname = nickname;
    }

    public @NotNull(message = "Email cannot be blank") @Email(message = "Incorrect email format") @NotBlank(message = "Email cannot be blank") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email cannot be blank") @Email(message = "Incorrect email format") @NotBlank(message = "Email cannot be blank") String email) {
        this.email = email;
    }

    public @NotNull(message = "Password cannot be blank") @NotBlank(message = "Password cannot be blank") @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password cannot be blank") @NotBlank(message = "Password cannot be blank") @Size(min = 8) String password) {
        this.password = password;
    }
}
