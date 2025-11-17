package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.PurchaseDto;
import org.jala.university.application.dto.UserDto;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.application.service.PurchaseService;
import org.jala.university.application.service.UserService;
import org.jala.university.application.session.SessionManager;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.presentation.CreditCardView;
import org.jala.university.presentation.components.TransactionCard;
import org.jala.university.presentation.context.CardUpdateContext;
import org.jala.university.presentation.context.InvoiceContext;
import org.jala.university.presentation.context.UserContext;

import java.util.Optional;

import java.text.NumberFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HomeController extends BaseController {

    @FXML
    private Label userNameLabel;

    @FXML
    private Label cardHolderLabel;

    @FXML
    private Label maskedCardLabel;

    @FXML
    private Label availableLimitLabel;

    @FXML
    private VBox cardContainer;

    @FXML
    private VBox currentCardDisplay;

    @FXML
    private VBox addCardDisplay;

    @FXML
    private VBox emptyStateContainer;

    @FXML
    private VBox transactionsContainer;

    @FXML
    private VBox transactionsList;

    @FXML
    private VBox emptyTransactionsState;

    @FXML
    private Button prevCardButton;

    @FXML
    private Button nextCardButton;

    @FXML
    private HBox carouselIndicators;

    private UUID userId;
    private UserService userService;
    private CreditCardService creditCardService;
    private PurchaseService purchaseService;
    private List<CreditCardDto> userCards;
    private int currentCardIndex = 0;
    private final SessionManager sessionManager = SessionManager.getInstance();

    public void setUserService(UserService service) {
        this.userService = service;
    }

    public void setCreditCardService(CreditCardService service) {
        this.creditCardService = service;
    }

    public void setPurchaseService(PurchaseService service) {
        this.purchaseService = service;
    }

    @FXML
    private void initialize() {
        loadServices();
    }

    @Override
    public void setContext(org.jala.university.commons.presentation.ViewContext context) {
        try {
            System.out.println("HomeController.setContext() START");
            super.setContext(context);
            System.out.println("HomeController.setContext() - calling loadData()");
            loadData();
            System.out.println("HomeController.setContext() COMPLETED");
        } catch (Exception e) {
            System.err.println("ERROR in HomeController.setContext(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void loadData() {
        try {
            System.out.println("HomeController.loadData() START");
            System.out.println("Context: " + getContext());

            if (getContext() instanceof UserContext ctx) {
                userId = ctx.getUserId();
                System.out.println("User ID: " + userId);
                loadUserData();
                System.out.println("HomeController.loadData() COMPLETED SUCCESSFULLY");
            } else {
                System.out.println("Context is NOT UserContext!");
            }
        } catch (Exception e) {
            System.err.println("ERROR in HomeController.loadData(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void loadServices() {
        if (userService == null) {
            userService = AppConfig.getInstance().getUserService();
        }
        if (creditCardService == null) {
            creditCardService = AppConfig.getInstance().getCreditCardService();
        }
        if (purchaseService == null) {
            purchaseService = AppConfig.getInstance().getPurchaseService();
        }
    }

    private void loadUserData() {
        System.out.println("loadUserData() - userService: " + userService + ", userId: " + userId);

        if (userService != null && userId != null) {
            UserDto user = userService.findById(userId);
            System.out.println("User loaded: " + user);

            if (user != null) {
                String firstName = user.getName().split(" ")[0];
                System.out.println("Setting name to: Olá, " + firstName);
                userNameLabel.setText("Olá, " + firstName);
            }
        }

        loadCreditCards();
    }

    private void loadCreditCards() {
        System.out.println("loadCreditCards() - creditCardService: " + creditCardService + ", userId: " + userId);

        if (creditCardService != null && userId != null) {
            userCards = creditCardService.findByUserId(userId);
            System.out.println("Cards loaded: " + (userCards != null ? userCards.size() : "null"));

            if (userCards == null || userCards.isEmpty()) {
                System.out.println("No cards found - showing empty state");
                showEmptyState();
            } else {
                System.out.println("Cards found - showing card state");
                showCardState();
                updateCardDisplay();
                updateCarouselControls();
                loadTransactions();
            }
        } else {
            System.out.println("Cannot load cards - service or userId is null");
        }
    }

    private void loadTransactions() {
        if (purchaseService == null || userCards == null || userCards.isEmpty() || currentCardIndex >= userCards.size()) {
            showEmptyTransactions();
            return;
        }

        CreditCardDto currentCard = userCards.get(currentCardIndex);
        List<PurchaseDto> purchases = purchaseService.findRecentByCreditCardId(currentCard.getCredit_card_id(), 5);

        if (purchases == null || purchases.isEmpty()) {
            showEmptyTransactions();
            return;
        }

        showTransactionsWithData(purchases);
    }

    private void showEmptyTransactions() {
        transactionsContainer.setVisible(false);
        transactionsContainer.setManaged(false);
        emptyTransactionsState.setVisible(true);
        emptyTransactionsState.setManaged(true);
    }

    private void showTransactionsWithData(List<PurchaseDto> purchases) {
        transactionsList.getChildren().clear();

        for (PurchaseDto purchase : purchases) {
            TransactionCard card = new TransactionCard(purchase);
            transactionsList.getChildren().add(card);
        }

        transactionsContainer.setVisible(true);
        transactionsContainer.setManaged(true);
        emptyTransactionsState.setVisible(false);
        emptyTransactionsState.setManaged(false);
    }

    private void showEmptyState() {
        cardContainer.setVisible(false);
        cardContainer.setManaged(false);
        emptyStateContainer.setVisible(true);
        emptyStateContainer.setManaged(true);
        transactionsContainer.setVisible(false);
        transactionsContainer.setManaged(false);
    }

    private void showCardState() {
        cardContainer.setVisible(true);
        cardContainer.setManaged(true);
        emptyStateContainer.setVisible(false);
        emptyStateContainer.setManaged(false);
        transactionsContainer.setVisible(true);
        transactionsContainer.setManaged(true);
    }

    @FXML
    private void handlePreviousCard() {
        if (currentCardIndex > 0) {
            currentCardIndex--;
            updateCarouselControls();
            updateCardDisplay();
            loadTransactions();
        }
    }

    @FXML
    private void handleNextCard() {
        if (userCards != null && currentCardIndex < userCards.size()) {
            currentCardIndex++;
            updateCarouselControls();
            updateCardDisplay();

            if (currentCardIndex < userCards.size()) {
                loadTransactions();
            }
        }
    }

    private void updateCarouselControls() {
        if (userCards == null || userCards.isEmpty()) {
            return;
        }

        int totalPositions = userCards.size() + 1;

        if (prevCardButton != null && nextCardButton != null) {
            prevCardButton.setDisable(currentCardIndex == 0);
            nextCardButton.setDisable(currentCardIndex >= totalPositions - 1);
        }

        if (carouselIndicators != null) {
            carouselIndicators.getChildren().clear();
            for (int i = 0; i < totalPositions; i++) {
                Circle indicator = new Circle(5);
                if (i == currentCardIndex) {
                    indicator.setStyle("-fx-fill: #1565C0;");
                } else {
                    indicator.setStyle("-fx-fill: #E0E0E0;");
                }
                carouselIndicators.getChildren().add(indicator);
            }
        }
    }

    private void updateCardDisplay() {
        if (userCards == null || userCards.isEmpty()) {
            return;
        }

        if (currentCardIndex >= userCards.size()) {
            showAddCardDisplay();
            return;
        }

        showCurrentCardDisplay();

        CreditCardDto card = userCards.get(currentCardIndex);

        if (maskedCardLabel != null) {
            maskedCardLabel.setText(formatCardNumber(card.getMaskCardNumber()));
        }

        if (cardHolderLabel != null && card.getHolderName() != null) {
            cardHolderLabel.setText(card.getHolderName().toUpperCase());
        }

        if (availableLimitLabel != null) {
            Double availableLimit = creditCardService.calculateAvailableLimit(card.getCredit_card_id());
            availableLimitLabel.setText(formatCurrency(availableLimit));
        }
    }

    private void showAddCardDisplay() {
        if (currentCardDisplay != null) {
            currentCardDisplay.setVisible(false);
        }
        if (addCardDisplay != null) {
            addCardDisplay.setVisible(true);
        }
    }

    private void showCurrentCardDisplay() {
        if (addCardDisplay != null) {
            addCardDisplay.setVisible(false);
        }
        if (currentCardDisplay != null) {
            currentCardDisplay.setVisible(true);
        }
    }

    private String formatCardNumber(String maskedNumber) {
        if (maskedNumber == null || maskedNumber.length() < 4) {
            return "•••• •••• •••• ••••";
        }
        return maskedNumber;
    }

    @FXML
    private void handlePayInvoice() {
        if (userCards == null || userCards.isEmpty() || currentCardIndex >= userCards.size()) {
            System.out.println("No card available");
            return;
        }

        CreditCardDto currentCard = userCards.get(currentCardIndex);
        InvoiceContext context = new InvoiceContext(userId, currentCard.getCredit_card_id());
        ViewSwitcher.switchTo(CreditCardView.INVOICE_MONTHLY.getView(), context);
    }

    @FXML
    private void handleVirtualCard() {
        System.out.println("Virtual card clicked");
    }

    @FXML
    private void handleBlockCard() {
        System.out.println("Block card clicked");
    }

    @FXML
    private void handleAdjustLimit() {
        if (userId == null || userCards == null || userCards.isEmpty()) {
            System.err.println("User ID or cards not available");
            return;
        }

        CreditCardDto currentCard = userCards.get(currentCardIndex);
        CardUpdateContext context = new CardUpdateContext(
            userId,
            currentCard.getCardNumber(),
            currentCard.getMaskCardNumber()
        );
        ViewSwitcher.switchTo(CreditCardView.CARD_UPDATE.getView(), context);
    }

    @FXML
    private void handleViewFullStatement() {
        System.out.println("View full statement clicked");
    }

    @FXML
    private void handleRequestCard() {
        ViewSwitcher.switchTo(CreditCardView.CARD_TYPE_SELECTION.getView());
    }

    @FXML
    private void handleLogout() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Sair");
        confirmAlert.setHeaderText("Deseja realmente sair?");
        confirmAlert.setContentText("Você será redirecionado para a tela de login.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sessionManager.endSession();
            userId = null;
            userCards = null;
            currentCardIndex = 0;
            ViewSwitcher.switchTo(CreditCardView.LOGIN.getView());
        }
    }

    private String formatCurrency(Double value) {
        if (value == null) return "R$ 0,00";
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatter.format(value);
    }
}
