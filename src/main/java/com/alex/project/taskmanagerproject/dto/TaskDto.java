package com.alex.project.taskmanagerproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TaskDto {
    @NotNull(message = "Title cannot be null")
    @Size(max  = 100, message = "Title cannot be greater than 100 symbols")
    private String title;
    @NotNull(message = "Description cannot be null")
    @Size(max = 3000, message = "Description cannot be greater than 3000 symbols")
    private String description;
    private int priority;
    private String status;
    private String userUsername;
    @NotNull(message = "Deadline cannot be null")
    @Future(message = "Enter future date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    private int task_group_id;
    @NotNull(message = "Project cannot be null")
    private int project_id;
    @NotNull(message = "Creator cannot be null")
    private String creatorUsername;

    public TaskDto() {
    }
//
//    public TaskDto(Builder builder) {
//        this.title = builder.title;
//        this.description = builder.description;
//        this.priority = builder.priority;
//        this.status = builder.status;
//        this.userUsername = builder.user_id;
//        this.deadline = builder.deadline;
//        this.task_group_id = builder.task_group_id;
//        this.project_id = builder.project_id;
//        this.creatorUsername = builder.creatorUsername;
//    }

//    public static class Builder{
//        private final String title;
//        private final String description;
//        private final LocalDate deadline;
//        private final String creatorUsername;
//        private int project_id;
//
//        private int priority;
//        private String status;
//        private String user_id;
//        private int task_group_id;
//
//        public Builder(String title, String description, LocalDate deadline, int project_id, String creatorUsername) {
//            this.title = title;
//            this.description = description;
//            this.deadline = deadline;
//            this.creatorUsername = creatorUsername;
//        }
//
//        public Builder setPriority(int priority) {
//            this.priority = priority;
//            return this;
//        }
//
//        public Builder setStatus(String status) {
//            this.status = status;
//            return this;
//        }
//
//        public Builder setUser_id(String user_id) {
//            this.user_id = user_id;
//            return this;
//        }
//
//        public Builder setTask_group_id(int task_group_id) {
//            this.task_group_id = task_group_id;
//            return this;
//        }
//
//
//        public TaskDto build(){
//            return new TaskDto(this);
//        }
//    }


    public @NotNull(message = "Title cannot be null") @Size(max = 100, message = "Title cannot be greater than 100 symbols") String getTitle() {
        return title;
    }

    public void setTitle(@NotNull(message = "Title cannot be null") @Size(max = 100, message = "Title cannot be greater than 100 symbols") String title) {
        this.title = title;
    }

    public @NotNull(message = "Description cannot be null") @Size(max = 3000, message = "Description cannot be greater than 3000 symbols") String getDescription() {
        return description;
    }

    public void setDescription(@NotNull(message = "Description cannot be null") @Size(max = 3000, message = "Description cannot be greater than 3000 symbols") String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public @NotNull(message = "Deadline cannot be null") @Future(message = "Enter future date") LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(@NotNull(message = "Deadline cannot be null") @Future(message = "Enter future date") LocalDate deadline) {
        this.deadline = deadline;
    }

    public int getTask_group_id() {
        return task_group_id;
    }

    public void setTask_group_id(int task_group_id) {
        this.task_group_id = task_group_id;
    }

    @NotNull(message = "Project cannot be null")
    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(@NotNull(message = "Project cannot be null") int project_id) {
        this.project_id = project_id;
    }

    public @NotNull(message = "Creator cannot be null") String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(@NotNull(message = "Creator cannot be null") String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
}
