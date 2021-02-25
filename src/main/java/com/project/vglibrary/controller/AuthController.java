package com.project.vglibrary.controller;

import com.project.vglibrary.entity.User;
import com.project.vglibrary.payload.request.LoginRequest;
import com.project.vglibrary.payload.request.SignupRequest;
import com.project.vglibrary.payload.response.ApiResponse;
import com.project.vglibrary.payload.response.JwtAuthenticationResponse;
import com.project.vglibrary.repository.UserRepository;
import com.project.vglibrary.security.jwt.JwtTokenProvider;
import com.project.vglibrary.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequest signupRequest) {
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();

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
}
