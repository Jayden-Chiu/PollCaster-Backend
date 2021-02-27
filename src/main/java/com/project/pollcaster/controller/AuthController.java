package com.project.pollcaster.controller;

import com.project.pollcaster.entity.User;
import com.project.pollcaster.entity.UserDetailsImpl;
import com.project.pollcaster.exception.ResourceNotFoundException;
import com.project.pollcaster.payload.request.LoginRequest;
import com.project.pollcaster.payload.request.SignupUpdateRequest;
import com.project.pollcaster.payload.response.ApiResponse;
import com.project.pollcaster.payload.response.JwtAuthenticationResponse;
import com.project.pollcaster.repository.UserRepository;
import com.project.pollcaster.security.CurrentUser;
import com.project.pollcaster.security.jwt.JwtTokenProvider;
import com.project.pollcaster.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    // authenticate user
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    // create user
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupUpdateRequest signupUpdateRequest) {
        String username = signupUpdateRequest.getUsername();
        String password = signupUpdateRequest.getPassword();

        if (userRepository.existsByUsername(username)) {
            return new ResponseEntity(
                    new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        User user = userService.save(new User(username, password));

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(
                new ApiResponse(true, "User created successfully!")
        );
    }

    // Update user
    @PatchMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUser(@CurrentUser UserDetailsImpl currentUser,
                           @RequestBody @Valid SignupUpdateRequest signupUpdateRequest) {
        Long id = currentUser.getId();
        String oldUsername = currentUser.getUsername();
        String newUsername = signupUpdateRequest.getUsername();
        String newPassword = signupUpdateRequest.getPassword();

        if (!oldUsername.equals(newUsername) && userRepository.existsByUsername(newUsername)) {
            return new ResponseEntity(
                    new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User",
                "id", id));
        existingUser.setUsername(newUsername);
        existingUser.setPassword(newPassword);

        User newUser = userService.save(existingUser);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).body(
                new ApiResponse(true, "User updated successfully!")
        );
    }
}
