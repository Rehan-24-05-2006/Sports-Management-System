package com.example.sports.repository;

import com.example.sports.model.Sport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends MongoRepository<Sport, String> {
}