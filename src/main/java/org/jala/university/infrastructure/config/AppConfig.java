package org.jala.university.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.mapper.InvoiceMapper;
import org.jala.university.application.mapper.UserMapper;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.application.service.CreditCardServiceImpl;
import org.jala.university.application.service.InvoiceService;
import org.jala.university.application.service.InvoiceServiceImpl;
import org.jala.university.application.service.UserService;
import org.jala.university.application.service.UserServiceImpl;
import org.jala.university.domain.repository.CreditCardRepository;
import org.jala.university.domain.repository.InvoiceRepository;
import org.jala.university.domain.repository.PurchaseRepository;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.persistence.CreditCardRepositoryImpl;
import org.jala.university.infrastructure.persistence.InvoiceRepositoryImpl;
import org.jala.university.infrastructure.persistence.PurchaseRepositoryImpl;
import org.jala.university.infrastructure.persistence.UserRepositoryImpl;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

    private static AppConfig instance;
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;
    private final CreditCardRepository creditCardRepository;
    private final InvoiceRepository invoiceRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CreditCardService creditCardService;
    private final InvoiceService invoiceService;
    private final UserService userService;

    private AppConfig() {

        Dotenv dotenv = Dotenv.load();

        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String name = dotenv.get("DB_NAME");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASSWORD");

        System.out.println("DB_HOST: " + host);
        System.out.println("DB_PORT: " + port);
        System.out.println("DB_NAME: " + name);
        System.out.println("DB_USER: " + user);
        System.out.println("DB_PASSWORD: " + pass);

        String url = "jdbc:mysql://" + host + ":" + port + "/" + name;

        System.out.println("url: " + url);
        // Configura o EntityManagerFactory dinamicamente com base nas variáveis de ambiente
        Map<String, String> props = new HashMap<>();

        props.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        props.put("jakarta.persistence.jdbc.url", url);
        props.put("jakarta.persistence.jdbc.user", user);
        props.put("jakarta.persistence.jdbc.password", pass);
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");

        this.entityManagerFactory = Persistence.createEntityManagerFactory("credit_card_module", props);
        this.entityManager = entityManagerFactory.createEntityManager();
        this.creditCardRepository = new CreditCardRepositoryImpl(entityManager);
        this.invoiceRepository = new InvoiceRepositoryImpl(entityManager);
        this.purchaseRepository = new PurchaseRepositoryImpl(entityManager);
        this.userRepository = new UserRepositoryImpl(entityManager);
        this.creditCardService = new CreditCardServiceImpl(
                creditCardRepository,
                CreditCardMapper.getInstance()
        );
        this.invoiceService = new InvoiceServiceImpl(
                invoiceRepository,
                InvoiceMapper.getInstance()
        );
        this.userService = new UserServiceImpl(
                userRepository,
                UserMapper.getInstance()
        );
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

    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }

    public PurchaseRepository getPurchaseRepository() {
        return purchaseRepository;
    }

    public CreditCardService getCreditCardService() {
        return creditCardService;
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public UserService getUserService() {
        return userService;
    }

    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}