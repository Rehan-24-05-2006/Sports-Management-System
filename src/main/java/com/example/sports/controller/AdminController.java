package com.example.sports.controller;

import com.example.sports.model.Sport;
import com.example.sports.model.User;
import com.example.sports.model.Role;
import com.example.sports.repository.SportRepository;
import com.example.sports.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SportRepository sportRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //  Create Sport
    @PostMapping("/create-sport")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Sport createSport(@RequestBody Sport sport) {
        return sportRepository.save(sport);
    }

    //  Create Coach
    @PostMapping("/create-coach")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public User createCoach(@RequestBody User user) {

        user.setRole(Role.COACH);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    //  Delete Coach
    @DeleteMapping("/delete-coach/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteCoach(@PathVariable String id) {

        userRepository.deleteById(id);
        return "Coach Deleted Successfully";
    }

}