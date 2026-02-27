package com.example.sports.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sports")
@Data
public class Sport {

    @Id
    private String id;

    private String sportName;
}