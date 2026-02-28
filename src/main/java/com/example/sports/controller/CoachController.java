package com.example.sports.controller;

import com.example.sports.model.Player;
import com.example.sports.model.Team;
import com.example.sports.model.User;
import com.example.sports.repository.TeamRepository;
import com.example.sports.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/coach")
@RequiredArgsConstructor
public class CoachController {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;


    // Team create
    @PostMapping("/create-team")
    @PreAuthorize("hasRole('COACH')")
    public Team createTeam(@RequestBody Team team) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        team.setCoachId(coach.getId());

        return teamRepository.save(team);
    }

    // Get Teams
    @GetMapping("/my-teams")
    @PreAuthorize("hasRole('COACH')")
    public List<Team> getMyTeams() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        return teamRepository.findByCoachId(coach.getId());
    }

    // Players add API
    @PutMapping("/add-player/{teamId}")
    @PreAuthorize("hasRole('COACH')")
    public Team addPlayer(@PathVariable String teamId,
                          @RequestBody Player player) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Duplicate check by email
        boolean exists = team.getPlayers().stream()
                .anyMatch(p -> p.getEmail().equals(player.getEmail()));

        if (exists) {
            throw new RuntimeException("Player already exists in this team");
        }

        team.getPlayers().add(player);

        return teamRepository.save(team);
    }

    // Player delete API
    @DeleteMapping("/remove-player/{teamId}/{email}")
    @PreAuthorize("hasRole('COACH')")
    public Team removePlayer(@PathVariable String teamId,
                             @PathVariable String email) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (team.getPlayers() == null) {
            throw new RuntimeException("No players in this team");
        }

        boolean removed = team.getPlayers()
                .removeIf(player -> player.getEmail().equals(email));

        if (!removed) {
            throw new RuntimeException("Player not found");
        }

        return teamRepository.save(team);
    }

    // Player update API
    @PutMapping("/update-player/{teamId}/{email}")
    @PreAuthorize("hasRole('COACH')")
    public Team updatePlayer(@PathVariable String teamId,
                             @PathVariable String email,
                             @RequestBody Player updatedPlayer) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (team.getPlayers() == null) {
            throw new RuntimeException("No players in this team");
        }

        boolean found = false;

        for (Player player : team.getPlayers()) {
            if (player.getEmail().equals(email)) {

                player.setPlayerName(updatedPlayer.getPlayerName());
                player.setYear(updatedPlayer.getYear());
                player.setBranch(updatedPlayer.getBranch());
                player.setCollegeId(updatedPlayer.getCollegeId());
                player.setPhone(updatedPlayer.getPhone());

                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Player not found");
        }

        return teamRepository.save(team);
    }
}