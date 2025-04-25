package com.alex.project.taskmanagerproject.dto.response;

import com.alex.project.taskmanagerproject.entity.User;

import java.util.List;

public class ProjectResponseDto {
    private int id;
    private String title;
    private List<UserResponseDto> users;

    public ProjectResponseDto() {
    }

    public ProjectResponseDto(int id, String title, List<UserResponseDto> users) {
        this.id = id;
        this.title = title;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserResponseDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseDto> users) {
        this.users = users;
    }
}
