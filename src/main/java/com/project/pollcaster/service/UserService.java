package com.project.pollcaster.service;

import com.project.pollcaster.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User save(User user);

    User findByUsername(String username);
}
