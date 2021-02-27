package com.project.pollcaster.payload.response;

import com.project.pollcaster.entity.User;
import com.project.pollcaster.entity.UserDetailsImpl;

import java.time.Instant;

public class UserProfile {
    private Long id;
    private String username;
    private Instant createdAt;

    public UserProfile(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
