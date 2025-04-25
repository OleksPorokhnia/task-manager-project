package com.alex.project.taskmanagerproject.service;

import com.alex.project.taskmanagerproject.entity.UserSearchEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchIndexInitializer {

    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ElasticSearchIndexInitializer(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostConstruct
    public void setup(){
        IndexOperations indexOperations = elasticsearchOperations.indexOps(UserSearchEntity.class);

        if(!indexOperations.exists()){
            indexOperations.create();
            indexOperations.putMapping(indexOperations.createMapping());
        }
    }
}
