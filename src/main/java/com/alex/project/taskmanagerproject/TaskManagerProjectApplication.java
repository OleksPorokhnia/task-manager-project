package com.alex.project.taskmanagerproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "com.alex.project.taskmanagerproject.repository")
@EntityScan(basePackages = "com.alex.project.taskmanagerproject.entity")
public class TaskManagerProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerProjectApplication.class, args);
    }

}
