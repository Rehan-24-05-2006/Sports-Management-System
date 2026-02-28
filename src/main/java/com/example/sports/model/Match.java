package com.example.sports.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matches")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Match {

    @Id
    private String id;

    private String sportName;

    private String teamAId;
    private String teamBId;

    private String matchDate;
    private String venue;

    private String winnerTeamId;

    private String status;
    // SCHEDULED
    // COMPLETED
    // CANCELLED
}