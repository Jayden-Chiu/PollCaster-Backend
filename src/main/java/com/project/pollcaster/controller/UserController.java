package com.project.pollcaster.controller;

import com.project.pollcaster.entity.User;
import com.project.pollcaster.entity.UserDetailsImpl;
import com.project.pollcaster.exception.ResourceNotFoundException;
import com.project.pollcaster.payload.response.UserProfile;
import com.project.pollcaster.repository.UserRepository;
import com.project.pollcaster.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public UserProfile getCurrentUser(@CurrentUser UserDetailsImpl currentUser) {
        return new UserProfile(currentUser);
    }

    @GetMapping("/{id}")
    public UserProfile getUserById(@PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return new UserProfile(user);
    }
}
