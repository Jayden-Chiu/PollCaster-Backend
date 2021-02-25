package com.project.vglibrary.service;

import com.project.vglibrary.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User save(User user);

    User findByUsername(String username);
}
