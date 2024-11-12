package org.anne_marschner_project.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class to bootstrap the Spring Boot application.
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.anne_marschner_project")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
