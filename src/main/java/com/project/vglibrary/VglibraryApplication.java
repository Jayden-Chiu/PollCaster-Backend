package com.project.vglibrary;

import com.project.vglibrary.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class VglibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(VglibraryApplication.class, args);
    }

}
