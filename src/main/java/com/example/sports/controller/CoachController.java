package com.example.sports.controller;

import com.example.sports.model.Team;
import com.example.sports.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coach")
@RequiredArgsConstructor
public class CoachController {

    private final TeamRepository teamRepository;

    // ✅ Create Team
    @PostMapping("/create-team")
    @PreAuthorize("hasRole('COACH')")
    public Team createTeam(@RequestBody Team team) {
        return teamRepository.save(team);
    }
}