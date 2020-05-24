package com.example.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Employee("Bilbo", "baggins", "burglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo", "baggins","theif")));
        };
    }
}
/*
Spring Boot will run ALL CommandLineRunner "beans"
once the application context is loaded.

This runner will request a copy of the EmployeeRepository you just created.

Using it, it will create two entities and store them.
 */

/*
@Slf4j is a Lombok annotation to autocreate an
Slf4j-based LoggerFactory as log,
allowing us to log these newly created "employees".
 */
