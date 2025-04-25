package com.alex.project.taskmanagerproject.repository;

import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.entity.UserSearchEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSearchRepository extends ElasticsearchRepository<UserSearchEntity, String> {

    List<UserSearchEntity> findByUsernameContainingIgnoreCase(String prefix);
}
