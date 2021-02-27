package com.project.vglibrary.controller;

import com.project.vglibrary.entity.User;
import com.project.vglibrary.entity.UserDetailsImpl;
import com.project.vglibrary.exception.ResourceNotFoundException;
import com.project.vglibrary.payload.response.UserProfile;
import com.project.vglibrary.repository.UserRepository;
import com.project.vglibrary.security.CurrentUser;
import com.project.vglibrary.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
