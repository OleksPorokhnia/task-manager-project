package com.alex.project.taskmanagerproject.controllers;

import com.alex.project.taskmanagerproject.entity.UserSearchEntity;
import com.alex.project.taskmanagerproject.repository.UserSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserSearchController {

    @Autowired
    private UserSearchRepository userSearchRepository;

    @GetMapping("/search")
    public List<UserSearchEntity> search(@RequestParam("username") String username) {
        return userSearchRepository.findByUsernameContainingIgnoreCase(username);
    }
}
