package com.project.vglibrary.payload.response;

import com.project.vglibrary.entity.User;
import com.project.vglibrary.entity.UserDetailsImpl;

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
