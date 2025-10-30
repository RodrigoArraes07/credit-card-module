package org.jala.university.infrastructure.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.h2.tools.Server;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.application.service.CreditCardServiceImpl;
import org.jala.university.domain.repository.CreditCardRepository;
import org.jala.university.infrastructure.persistence.CreditCardRepositoryImpl;

public class AppConfig {

    private static AppConfig instance;
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;
    private final CreditCardRepository creditCardRepository;
    private final CreditCardService creditCardService;
    private Server h2WebServer;

    private AppConfig() {
        startH2Console();

        this.entityManagerFactory = Persistence.createEntityManagerFactory("credit_card_module");
        this.entityManager = entityManagerFactory.createEntityManager();
        this.creditCardRepository = new CreditCardRepositoryImpl(entityManager);
        this.creditCardService = new CreditCardServiceImpl(
                creditCardRepository,
                CreditCardMapper.getInstance()
        );
    }

    private void startH2Console() {
        try {
            h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
            h2WebServer.start();
            System.out.println("========================================");
            System.out.println("H2 Console disponível em: http://localhost:8082");
            System.out.println("JDBC URL: jdbc:h2:mem:creditcard");
            System.out.println("User: admin");
            System.out.println("Password: admin");
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar H2 Console: " + e.getMessage());
        }
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public CreditCardRepository getCreditCardRepository() {
        return creditCardRepository;
    }

    public CreditCardService getCreditCardService() {
        return creditCardService;
    }

    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
        if (h2WebServer != null && h2WebServer.isRunning(false)) {
            h2WebServer.stop();
            System.out.println("H2 Console encerrado");
        }
    }
}