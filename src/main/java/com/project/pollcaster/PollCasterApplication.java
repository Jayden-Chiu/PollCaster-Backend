package com.project.pollcaster;

import com.project.pollcaster.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class PollCasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PollCasterApplication.class, args);
    }

}
