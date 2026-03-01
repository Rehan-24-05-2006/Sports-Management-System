package com.example.sports.controller;

import com.example.sports.model.Match;
import com.example.sports.model.Player;
import com.example.sports.model.Team;
import com.example.sports.model.User;
import com.example.sports.repository.MatchRepository;
import com.example.sports.repository.TeamRepository;
import com.example.sports.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coach")
@RequiredArgsConstructor
public class CoachController {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;


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

    // Team Delete
    @DeleteMapping("/delete-team/{teamId}")
    @PreAuthorize("hasRole('COACH')")
    public String deleteTeam(@PathVariable String teamId) {

        teamRepository.deleteById(teamId);

        return "Team deleted successfully";
    }

    // Match Create
    @PostMapping("/create-match")
    @PreAuthorize("hasRole('COACH')")
    public Match createMatch(@RequestBody Match match) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        Team teamA = teamRepository.findById(match.getTeamAId())
                .orElseThrow(() -> new RuntimeException("Team A not found"));

        Team teamB = teamRepository.findById(match.getTeamBId())
                .orElseThrow(() -> new RuntimeException("Team B not found"));

        //  Same team check
        if (teamA.getId().equals(teamB.getId())) {
            throw new RuntimeException("Both teams cannot be same");
        }

        //  Same sport check
        if (!teamA.getSportName().equals(teamB.getSportName())) {
            throw new RuntimeException("Teams must belong to same sport");
        }

        //  Coach ownership check
        if (!teamA.getCoachId().equals(coach.getId()) ||
                !teamB.getCoachId().equals(coach.getId())) {
            throw new RuntimeException("You can only create match for your teams");
        }

        if (!teamA.getSportName().equals(match.getSportName())) {
            throw new RuntimeException("Sport mismatch");
        }

        match.setSportName(teamA.getSportName());
        match.setCoachId(coach.getId());
        match.setStatus("SCHEDULED");

        return matchRepository.save(match);
    }

    // update Match
    @PutMapping("/update-match/{matchId}")
    @PreAuthorize("hasRole('COACH')")
    public Match updateMatch(@PathVariable String matchId,
                             @RequestParam String winnerTeamId,
                             @RequestParam String status) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        match.setWinnerTeamId(winnerTeamId);
        match.setStatus(status);

        return matchRepository.save(match);
    }

    // Show All Matches
    @GetMapping("/my-matches")
    @PreAuthorize("hasRole('COACH')")
    public List<Match> getMyMatches(Authentication authentication) {

        String email = authentication.getName();
        User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        return matchRepository.findByCoachId(coach.getId());
    }


    // Delete Match
    @DeleteMapping("/delete-match/{matchId}")
    @PreAuthorize("hasRole('COACH')")
    public String deleteMatch(@PathVariable String matchId) {

        matchRepository.deleteById(matchId);

        return "Match deleted successfully";
    }

    // Match history
    @GetMapping("/team/{teamId}/matches")
    @PreAuthorize("hasRole('COACH')")
    public List<Match> getTeamMatches(@PathVariable String teamId) {

        return matchRepository.findByTeamAIdOrTeamBId(teamId, teamId);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('COACH')")
    public Map<String, Object> getDashboardData() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        List<Team> teams = teamRepository.findByCoachId(coach.getId());

        List<Match> matches = matchRepository.findByCoachId(coach.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("coachName", coach.getName());
        data.put("totalTeams", teams.size());
        data.put("totalMatches", matches.size());
        data.put("teams", teams);
        data.put("matches", matches);

        return data;
    }

}