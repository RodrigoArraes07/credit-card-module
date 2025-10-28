package org.jala.university;

import javafx.application.Application;
import org.jala.university.infrastructure.config.EnvConfig;
import org.jala.university.presentation.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org.jala.university.domain.entity")
@EnableJpaRepositories(basePackages = "org.jala.university.domain.repository")
public class MainApp {

    private static ConfigurableApplicationContext springContext;

    static {
        EnvConfig.load();
    }

    public static void main(String[] args) {
        springContext = new SpringApplicationBuilder(MainApp.class)
                .headless(false)
                .run(args);

        Application.launch(MainView.class, args);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}
