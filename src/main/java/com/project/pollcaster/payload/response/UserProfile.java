package com.project.pollcaster.payload.response;

import com.project.pollcaster.entity.User;
import com.project.pollcaster.entity.UserDetailsImpl;

public class UserProfile {
    private Long id;
    private String username;

    public UserProfile(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public UserProfile(UserDetailsImpl user) {
        this.id = user.getId();
        this.username = user.getUsername();
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
}