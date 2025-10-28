package org.jala.university.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    static {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            dotenv.entries().forEach(entry ->
                    System.setProperty(entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            System.err.println("Warning: Could not load .env file - " + e.getMessage());
        }
    }

    public static void load() {
    }
}
