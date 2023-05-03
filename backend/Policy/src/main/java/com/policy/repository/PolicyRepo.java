package com.policy.repository;

import com.policy.entity.Policy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepo extends MongoRepository<Policy,String> {
    @Query(value = "{}", count = true)
    public Long countAllDocuments();
}
