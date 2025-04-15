package com.alex.project.taskmanagerproject.repository;

import com.alex.project.taskmanagerproject.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNickname(String username);
    Optional<User> findByEmail(String email);
    boolean existsByNickname(String username);
    boolean existsByEmail(String email);
}
