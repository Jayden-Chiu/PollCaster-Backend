package com.project.vglibrary.service;

import com.project.vglibrary.entity.User;

public interface UserService {
    User save(User user);

    User findByUsername(String username);
}
