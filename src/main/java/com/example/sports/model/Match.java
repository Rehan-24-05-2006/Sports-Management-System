package com.example.sports.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matches")
@Data
public class Match {

    @Id
    private String id;

    private String sportId;
    private String team1Id;
    private String team2Id;

    private String matchDate;

    private String status; // SCHEDULED, COMPLETED, CANCELLED
    private String winnerTeamId;
}