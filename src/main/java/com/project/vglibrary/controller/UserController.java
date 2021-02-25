package com.project.vglibrary.controller;

import com.project.vglibrary.entity.User;
import com.project.vglibrary.exception.ResourceNotFoundException;
import com.project.vglibrary.repository.UserRepository;
import com.project.vglibrary.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    // Delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id",
                id));

        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }
}
