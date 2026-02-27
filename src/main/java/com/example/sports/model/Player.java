package com.example.sports.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private String playerName;
    private String year;
    private String branch;
    private String collegeId;
    private String email;
    private String phone;
}