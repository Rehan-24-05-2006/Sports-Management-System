package com.example.sports.repository;

import com.example.sports.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {

    List<Match> findBySportName(String sportName);

    List<Match> findByTeamAIdOrTeamBId(String teamAId, String teamBId);

}