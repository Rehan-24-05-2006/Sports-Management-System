package com.example.sports.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "teams")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    private String id;

    private String teamName;
    private String branch;
    private String sportName;

    private String coachId;

    private List<Player> players = new ArrayList<>();
}