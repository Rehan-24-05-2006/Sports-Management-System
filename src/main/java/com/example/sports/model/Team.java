package com.example.sports.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "teams")
@Data
public class Team {

    @Id
    private String id;

    private String teamName;
    private String branch;
    private String sportId;
    private String coachId;

    private List<Player> players;
}