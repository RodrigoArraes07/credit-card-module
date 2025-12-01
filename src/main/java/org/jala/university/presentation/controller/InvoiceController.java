package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.InvoiceDetailDto;
import org.jala.university.application.dto.InvoiceTimelineDto;
import org.jala.university.application.dto.PaymentResponseDto;
import org.jala.university.application.dto.PurchaseInstallmentDto;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.application.service.InvoiceService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewContext;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.enums.PaymentMethod;
import org.jala.university.infrastructure.config.AppConfig;
import org.jala.university.presentation.CreditCardView;
import org.jala.university.presentation.components.ConfirmAnticipationDialog;
import org.jala.university.presentation.components.PaymentInstructionsDialog;
import org.jala.university.presentation.components.PaymentMethodDialog;
import org.jala.university.presentation.components.invoice.InstallmentCard;
import org.jala.university.presentation.components.invoice.TimelineGraph;
import org.jala.university.presentation.context.CardSecurityContext;
import org.jala.university.presentation.context.InvoiceContext;
import org.jala.university.presentation.context.UserContext;
import org.jala.university.presentation.utils.CurrencyFormatter;
import org.jala.university.presentation.utils.DateFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class InvoiceController extends BaseController {

    private final InvoiceService invoiceService;
    private final CreditCardService creditCardService;

    @FXML private ComboBox<String> cardSelector;
    @FXML private Label yearLabel;
    @FXML private Label previousInvoiceAmount;
    @FXML private Label currentInvoiceAmount;
    @FXML private Label nextInvoiceAmount;
    @FXML private TimelineGraph timelineGraph;
    @FXML private Label previousMonthLabel;
    @FXML private Label currentMonthLabel;
    @FXML private Label nextMonthLabel;
    @FXML private Label dueDateLabel;
    @FXML private Label closingDateLabel;
    @FXML private Label installmentsTotalLabel;
    @FXML private VBox installmentsList;
    @FXML private VBox emptyState;
    @FXML private javafx.scene.control.Button payInvoiceButton;
    @FXML private javafx.scene.control.Button prevInvoiceButton;
    @FXML private javafx.scene.control.Button nextInvoiceButton;
    @FXML private VBox paidInvoiceBadge;
    @FXML private Label paidInvoiceDetails;
    @FXML private Label paidInvoiceLimitInfo;

    private UUID creditCardId;
    private UUID userId;
    private List<CreditCardDto> userCards;
    private InvoiceTimelineDto timelineData;
    private Label selectedMonthLabel;
    private List<InvoiceDetailDto> allInvoices;
    private int currentInvoiceIndex = 0;

    public InvoiceController() {
        this.invoiceService = AppConfig.getInstance().getInvoiceService();
        this.creditCardService = AppConfig.getInstance().getCreditCardService();
    }

    @FXML
    private void initialize() {
        System.out.println("InvoiceController.initialize() called");
    }

    @Override
    public void setContext(ViewContext context) {
        System.out.println("InvoiceController.setContext() called");
        super.setContext(context);
        loadData();
    }

    private void loadData() {
        if (getContext() instanceof InvoiceContext ctx) {
            this.userId = ctx.getUserId();
            this.creditCardId = ctx.getCreditCardId();
            loadUserCards();
            loadInvoiceTimeline();
        }
    }

    private void loadUserCards() {
        try {
            userCards = creditCardService.findByUserId(userId);

            if (userCards != null && !userCards.isEmpty()) {
                cardSelector.getItems().clear();

                for (CreditCardDto card : userCards) {
                    String cardDisplay = String.format("%s •••• %s",
                            card.getTargetType() != null ? card.getTargetType().toString() : "Card",
                            card.getMaskCardNumber() != null && card.getMaskCardNumber().length() >= 4 ?
                                    card.getMaskCardNumber().substring(card.getMaskCardNumber().length() - 4) : "****");
                    cardSelector.getItems().add(cardDisplay);
                }

                int currentIndex = findCardIndex(creditCardId);
                if (currentIndex >= 0) {
                    cardSelector.getSelectionModel().select(currentIndex);
                }

                cardSelector.setOnAction(e -> {
                    int selectedIndex = cardSelector.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0 && userCards != null && selectedIndex < userCards.size()) {
                        CreditCardDto selectedCard = userCards.get(selectedIndex);
                        this.creditCardId = selectedCard.getCredit_card_id();
                        loadInvoiceTimeline();
                    }
                });
            }

            System.out.println("Loaded " + (userCards != null ? userCards.size() : 0) + " cards");
        } catch (Exception e) {
            System.err.println("Error loading user cards: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int findCardIndex(UUID cardId) {
        if (userCards == null) return -1;
        for (int i = 0; i < userCards.size(); i++) {
            if (userCards.get(i).getCredit_card_id().equals(cardId)) {
                return i;
            }
        }
        return -1;
    }

    private void loadInvoiceTimeline() {
        System.out.println("loadInvoiceTimeline() called for creditCardId: " + creditCardId);

        allInvoices = invoiceService.getAllInvoicesByCreditCard(creditCardId);

        if (allInvoices == null || allInvoices.isEmpty()) {
            System.out.println("No invoices found - showing empty state");
            showEmptyState();
            return;
        }

        System.out.println("Found " + allInvoices.size() + " invoices - loading UI");
        hideEmptyState();

        findCurrentInvoiceIndex();
        updateInvoiceDisplay();
    }

    private void findCurrentInvoiceIndex() {
        LocalDate today = LocalDate.now();

        for (int i = 0; i < allInvoices.size(); i++) {
            InvoiceDetailDto invoice = allInvoices.get(i);
            boolean isInvoiceClosed = invoice.getClosingDate().isBefore(today) || invoice.getClosingDate().isEqual(today);
            boolean isPaid = invoice.getIsPaid() != null && invoice.getIsPaid();

            if (isInvoiceClosed && !isPaid) {
                currentInvoiceIndex = i;
                return;
            }
        }

        for (int i = 0; i < allInvoices.size(); i++) {
            InvoiceDetailDto invoice = allInvoices.get(i);
            if (invoice.getIsPaid() == null || !invoice.getIsPaid()) {
                currentInvoiceIndex = i;
                return;
            }
        }

        currentInvoiceIndex = allInvoices.size() > 0 ? allInvoices.size() - 1 : 0;
    }

    private void updateInvoiceDisplay() {
        if (allInvoices == null || allInvoices.isEmpty()) {
            return;
        }

        InvoiceDetailDto current = allInvoices.get(currentInvoiceIndex);
        InvoiceDetailDto previous = currentInvoiceIndex > 0 ? allInvoices.get(currentInvoiceIndex - 1) : null;
        InvoiceDetailDto next = currentInvoiceIndex < allInvoices.size() - 1 ? allInvoices.get(currentInvoiceIndex + 1) : null;

        yearLabel.setText(String.valueOf(current.getClosingDate().getYear()));

        updateAmountLabels(previous, current, next);
        updateMonthLabels(previous, current, next);
        setupTimelineGraph(previous, current, next);
        loadInvoiceDetails(current);
        loadPurchaseInstallments(current.getInstallments());
        updateNavigationControls();

        timelineData = InvoiceTimelineDto.builder()
                .previousInvoice(previous)
                .currentInvoice(current)
                .nextInvoice(next)
                .year(current.getClosingDate().getYear())
                .build();
    }

    private void updateNavigationControls() {
        if (prevInvoiceButton != null && nextInvoiceButton != null) {
            prevInvoiceButton.setDisable(currentInvoiceIndex == 0);
            nextInvoiceButton.setDisable(currentInvoiceIndex >= allInvoices.size() - 1);
        }
    }

    @FXML
    private void handlePreviousInvoice() {
        if (currentInvoiceIndex > 0) {
            currentInvoiceIndex--;
            updateInvoiceDisplay();
        }
    }

    @FXML
    private void handleNextInvoice() {
        if (allInvoices != null && currentInvoiceIndex < allInvoices.size() - 1) {
            currentInvoiceIndex++;
            updateInvoiceDisplay();
        }
    }

    private void updateAmountLabels(InvoiceDetailDto previous, InvoiceDetailDto current, InvoiceDetailDto next) {
        if (previous != null) {
            boolean previousPaid = previous.getIsPaid() != null && previous.getIsPaid();
            if (previousPaid) {
                previousInvoiceAmount.setText("PAGA");
                previousInvoiceAmount.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #4CAF50;");
            } else {
                previousInvoiceAmount.setText(CurrencyFormatter.format(previous.getTotalAmount()));
                previousInvoiceAmount.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #2E7D32;");
            }
        } else {
            previousInvoiceAmount.setText("R$ 0,00");
            previousInvoiceAmount.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #2E7D32;");
        }

        boolean currentPaid = current.getIsPaid() != null && current.getIsPaid();
        if (currentPaid) {
            currentInvoiceAmount.setText("PAGA");
            currentInvoiceAmount.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #4CAF50;");
        } else {
            currentInvoiceAmount.setText(CurrencyFormatter.format(current.getTotalAmount()));
            currentInvoiceAmount.setStyle("-fx-font-size: 18px; -fx-font-weight: 700; -fx-text-fill: #7B1FA2;");
        }

        if (next != null) {
            boolean nextPaid = next.getIsPaid() != null && next.getIsPaid();
            if (nextPaid) {
                nextInvoiceAmount.setText("PAGA");
                nextInvoiceAmount.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #4CAF50;");
            } else {
                nextInvoiceAmount.setText(CurrencyFormatter.format(next.getTotalAmount()));
                nextInvoiceAmount.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #9E9E9E;");
            }
        } else {
            nextInvoiceAmount.setText("R$ 0,00");
            nextInvoiceAmount.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #9E9E9E;");
        }
    }

    private void updateMonthLabels(InvoiceDetailDto previous, InvoiceDetailDto current, InvoiceDetailDto next) {
        previousMonthLabel.setText(previous != null ? DateFormatter.formatMonth(previous.getClosingDate()) : "-");
        currentMonthLabel.setText(DateFormatter.formatMonth(current.getClosingDate()));
        nextMonthLabel.setText(next != null ? DateFormatter.formatMonth(next.getClosingDate()) : "-");

        setupMonthClickHandlers(previous, current, next);
        updateMonthLabelsStyle(currentMonthLabel);
    }

    private void setupTimelineGraph(InvoiceDetailDto previous, InvoiceDetailDto current, InvoiceDetailDto next) {
        timelineGraph.setData(previous, current, next);
        timelineGraph.setOnInvoiceSelected(this::onInvoiceSelectedFromGraph);
    }

    private void onInvoiceSelectedFromGraph(InvoiceDetailDto invoice) {
        Label correspondingLabel = getMonthLabelForInvoice(invoice);
        if (correspondingLabel != null) {
            updateMonthLabelsStyle(correspondingLabel);
        }
        loadInvoiceDetails(invoice);
        loadPurchaseInstallments(invoice.getInstallments());

        timelineData.setCurrentInvoice(invoice);
    }

    private void setupMonthClickHandlers(InvoiceDetailDto previous, InvoiceDetailDto current, InvoiceDetailDto next) {
        setupMonthLabel(previousMonthLabel, previous, "#2E7D32");
        setupMonthLabel(currentMonthLabel, current, "#7B1FA2");
        setupMonthLabel(nextMonthLabel, next, "#9E9E9E");
    }

    private void setupMonthLabel(Label label, InvoiceDetailDto invoice, String color) {
        if (invoice != null) {
            label.setCursor(Cursor.HAND);
            label.setOnMouseClicked(e -> selectInvoiceByMonth(invoice, label));
            label.setOnMouseEntered(e -> {
                if (selectedMonthLabel != label) {
                    label.setStyle(label.getStyle() + " -fx-text-fill: " + color + "; -fx-underline: true;");
                }
            });
            label.setOnMouseExited(e -> {
                if (selectedMonthLabel != label) {
                    resetMonthLabelStyle(label);
                }
            });
        } else {
            label.setCursor(Cursor.DEFAULT);
            label.setOnMouseClicked(null);
            label.setOnMouseEntered(null);
            label.setOnMouseExited(null);
        }
    }

    private void selectInvoiceByMonth(InvoiceDetailDto invoice, Label monthLabel) {
        if (invoice == null) return;

        updateMonthLabelsStyle(monthLabel);
        loadInvoiceDetails(invoice);
        loadPurchaseInstallments(invoice.getInstallments());

        timelineData.setCurrentInvoice(invoice);
        timelineGraph.setData(timelineData.getPreviousInvoice(), invoice, timelineData.getNextInvoice());
    }

    private void updateMonthLabelsStyle(Label selectedLabel) {
        selectedMonthLabel = selectedLabel;

        if (previousMonthLabel != null && timelineData != null && timelineData.getPreviousInvoice() != null) {
            if (selectedLabel == previousMonthLabel) {
                applySelectedStyle(previousMonthLabel, "#2E7D32");
            } else {
                resetMonthLabelStyle(previousMonthLabel);
            }
        }

        if (currentMonthLabel != null && timelineData != null && timelineData.getCurrentInvoice() != null) {
            if (selectedLabel == currentMonthLabel) {
                applySelectedStyle(currentMonthLabel, "#7B1FA2");
            } else {
                currentMonthLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #424242;");
            }
        }

        if (nextMonthLabel != null && timelineData != null && timelineData.getNextInvoice() != null) {
            if (selectedLabel == nextMonthLabel) {
                applySelectedStyle(nextMonthLabel, "#616161");
            } else {
                resetMonthLabelStyle(nextMonthLabel);
            }
        }
    }

    private void applySelectedStyle(Label label, String color) {
        String rgba = colorToRgba(color);
        label.setStyle(String.format("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: %s; " +
                "-fx-background-color: %s; -fx-background-radius: 8; -fx-padding: 4 8;", color, rgba));
    }

    private void resetMonthLabelStyle(Label label) {
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #757575;");
    }

    private String colorToRgba(String hex) {
        return switch (hex) {
            case "#2E7D32" -> "rgba(46, 125, 50, 0.1)";
            case "#7B1FA2" -> "rgba(123, 31, 162, 0.1)";
            case "#616161" -> "rgba(158, 158, 158, 0.1)";
            default -> "rgba(0, 0, 0, 0.1)";
        };
    }

    private Label getMonthLabelForInvoice(InvoiceDetailDto invoice) {
        if (invoice == null || timelineData == null) return null;

        if (timelineData.getPreviousInvoice() != null && invoice.getId().equals(timelineData.getPreviousInvoice().getId())) {
            return previousMonthLabel;
        } else if (timelineData.getCurrentInvoice() != null && invoice.getId().equals(timelineData.getCurrentInvoice().getId())) {
            return currentMonthLabel;
        } else if (timelineData.getNextInvoice() != null && invoice.getId().equals(timelineData.getNextInvoice().getId())) {
            return nextMonthLabel;
        }

        return null;
    }

    private void loadInvoiceDetails(InvoiceDetailDto invoice) {
        if (invoice == null) return;

        dueDateLabel.setText(DateFormatter.formatFullDate(invoice.getDueDate()));
        closingDateLabel.setText(DateFormatter.formatFullDate(invoice.getClosingDate()));
        installmentsTotalLabel.setText(CurrencyFormatter.format(invoice.getTotalAmount()));

        updatePayInvoiceButton(invoice);
        updatePaidInvoiceBadge(invoice);
    }

    private void updatePaidInvoiceBadge(InvoiceDetailDto invoice) {
        if (paidInvoiceBadge == null || invoice == null) return;

        boolean isPaid = invoice.getIsPaid() != null && invoice.getIsPaid();

        if (isPaid) {
            paidInvoiceBadge.setVisible(true);
            paidInvoiceBadge.setManaged(true);

            if (paidInvoiceDetails != null && invoice.getPaymentDate() != null && invoice.getPaymentMethod() != null) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
                String paymentDateStr = invoice.getPaymentDate().format(dateFormatter);
                String paymentMethodStr = invoice.getPaymentMethod().getDisplayName();
                paidInvoiceDetails.setText("Paga em " + paymentDateStr + " via " + paymentMethodStr);
            }

            if (paidInvoiceLimitInfo != null) {
                String limitAmount = CurrencyFormatter.format(invoice.getTotalAmount());
                paidInvoiceLimitInfo.setText("Seu limite de " + limitAmount + " já foi liberado e está disponível");
            }
        } else {
            paidInvoiceBadge.setVisible(false);
            paidInvoiceBadge.setManaged(false);
        }
    }

    private void updatePayInvoiceButton(InvoiceDetailDto invoice) {
        if (payInvoiceButton == null || invoice == null) return;

        LocalDate today = LocalDate.now();
        boolean isInvoiceClosed = invoice.getClosingDate().isBefore(today) || invoice.getClosingDate().isEqual(today);
        boolean isPaid = invoice.getIsPaid() != null && invoice.getIsPaid();

        if (isPaid) {
            payInvoiceButton.setText("FATURA PAGA");
            payInvoiceButton.setDisable(true);
            payInvoiceButton.setStyle(
                    "-fx-pref-height: 56; -fx-font-size: 15px; -fx-font-weight: 700; " +
                    "-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-cursor: default;");
        } else if (!isInvoiceClosed) {
            payInvoiceButton.setText("FATURA EM ABERTO");
            payInvoiceButton.setDisable(true);
            payInvoiceButton.setStyle(
                    "-fx-pref-height: 56; -fx-font-size: 15px; -fx-font-weight: 700; " +
                    "-fx-background-color: #FFF3E0; -fx-text-fill: #F57C00; -fx-cursor: default;");
        } else {
            payInvoiceButton.setText("PAGAR FATURA COMPLETA");
            payInvoiceButton.setDisable(false);
            payInvoiceButton.setStyle(
                    "-fx-pref-height: 56; -fx-font-size: 15px; -fx-font-weight: 700; " +
                    "-fx-background-color: #1565C0; -fx-text-fill: white; -fx-cursor: hand;");
        }
    }

    private void loadPurchaseInstallments(List<PurchaseInstallmentDto> installments) {
        installmentsList.getChildren().clear();

        if (installments == null || installments.isEmpty()) {
            showEmptyState();
            return;
        }

        hideEmptyState();

        for (PurchaseInstallmentDto installment : installments) {
            InstallmentCard card = new InstallmentCard(installment);
            installmentsList.getChildren().add(card);
        }
    }

    private void showEmptyState() {
        if (emptyState != null) {
            emptyState.setVisible(true);
            emptyState.setManaged(true);
        }
        if (installmentsList != null) {
            installmentsList.getChildren().clear();
        }
    }

    private void hideEmptyState() {
        if (emptyState != null) {
            emptyState.setVisible(false);
            emptyState.setManaged(false);
        }
    }

    @FXML
    private void handleBack() {
        try {
            System.out.println("handleBack() called - userId: " + userId);
            if (userId != null) {
                UserContext context = new UserContext(userId);
                System.out.println("Creating UserContext: " + context);
                ViewSwitcher.switchTo(CreditCardView.HOME.getView(), context);
                System.out.println("ViewSwitcher.switchTo() completed");
            } else {
                System.out.println("userId is null - navigating without context");
                ViewSwitcher.switchTo(CreditCardView.HOME.getView());
            }
            System.out.println("handleBack() RETURNING NORMALLY");
        } catch (Exception e) {
            System.err.println("ERROR in handleBack(): " + e.getMessage());
            e.printStackTrace();
            showErrorMessage("Erro ao voltar: " + e.getMessage());
        }
        System.out.println("handleBack() METHOD ENDED");
    }

    @FXML
    private void handleDownload() {
        if (timelineData == null || timelineData.getCurrentInvoice() == null) {
            showErrorMessage("Nenhuma fatura disponível para download");
            return;
        }

        try {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Salvar Extrato da Fatura (PDF)");

            DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyy_MM", Locale.forLanguageTag("pt-BR"));
            fileChooser.setInitialFileName("extrato_fatura_" +
                    timelineData.getCurrentInvoice().getClosingDate().format(fileFormatter) + ".pdf");

            fileChooser.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf"));

            java.io.File file = fileChooser.showSaveDialog(installmentsList.getScene().getWindow());

            if (file != null) {
                org.jala.university.presentation.utils.InvoicePdfGenerator.generateInvoicePdf(
                        timelineData.getCurrentInvoice(),
                        file.getAbsolutePath()
                );
                showSuccessMessage("Extrato em PDF salvo com sucesso!");
                System.out.println("Invoice PDF downloaded to: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showErrorMessage("Erro ao salvar extrato: " + e.getMessage());
            System.err.println("Error downloading invoice: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

    private void showErrorMessage(String message) {
        System.err.println("ERROR: " + message);
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handlePayFullInvoice() {
        if (timelineData == null || timelineData.getCurrentInvoice() == null) {
            showErrorAlert("Erro", "Nenhuma fatura disponível para pagamento.");
            return;
        }

        InvoiceDetailDto currentInvoice = timelineData.getCurrentInvoice();

        if (currentInvoice.getIsPaid() != null && currentInvoice.getIsPaid()) {
            showErrorAlert("Fatura já paga", "Esta fatura já foi paga.");
            return;
        }

        PaymentMethodDialog paymentDialog = new PaymentMethodDialog();
        Optional<PaymentMethod> selectedMethod = paymentDialog.showAndWait();

        if (selectedMethod.isEmpty()) {
            System.out.println("Payment cancelled by user");
            return;
        }

        try {
            PaymentResponseDto response = invoiceService.processPayment(
                    currentInvoice.getId(),
                    selectedMethod.get()
            );

            if (response.getSuccess()) {
                PaymentInstructionsDialog instructionsDialog = new PaymentInstructionsDialog(selectedMethod.get());
                instructionsDialog.showAndWait();

                showSuccessAlert(
                        "Pagamento Confirmado",
                        "Fatura paga com sucesso via " + selectedMethod.get().getDisplayName() + "!"
                );

                loadInvoiceTimeline();
            } else {
                showErrorAlert("Erro no Pagamento", response.getMessage());
            }
        } catch (Exception e) {
            showErrorAlert("Erro", "Erro ao processar pagamento: " + e.getMessage());
            System.err.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInstallPurchase() {
        System.out.println("Install purchase");
    }

    @FXML
    private void handleAnticipatePix() {
        if (timelineData == null || timelineData.getCurrentInvoice() == null) {
            showErrorAlert("Erro", "Nenhuma fatura disponível para antecipação.");
            return;
        }

        InvoiceDetailDto currentInvoice = timelineData.getCurrentInvoice();

        if (currentInvoice.getIsPaid() != null && currentInvoice.getIsPaid()) {
            showErrorAlert("Fatura já paga", "Esta fatura já foi paga. Não é possível antecipar uma fatura que já foi paga.");
            return;
        }

        if (currentInvoice.getTotalAmount() == null || currentInvoice.getTotalAmount() <= 0) {
            showErrorAlert("Valor inválido", "Esta fatura não possui valor a ser pago.");
            return;
        }

        ConfirmAnticipationDialog confirmDialog = new ConfirmAnticipationDialog(
                CurrencyFormatter.format(currentInvoice.getTotalAmount()),
                DateFormatter.formatFullDate(currentInvoice.getDueDate())
        );

        confirmDialog.showAndWait().ifPresent(confirmed -> {
            if (confirmed) {
                PaymentMethodDialog paymentDialog = new PaymentMethodDialog();
                Optional<PaymentMethod> selectedMethod = paymentDialog.showAndWait();

                if (selectedMethod.isPresent()) {
                    processAnticipation(currentInvoice, selectedMethod.get());
                }
            }
        });
    }

    private void processAnticipation(InvoiceDetailDto invoice, PaymentMethod method) {
        try {
            PaymentResponseDto response = invoiceService.processPayment(
                    invoice.getId(),
                    method
            );

            if (response.getSuccess()) {
                PaymentInstructionsDialog instructionsDialog = new PaymentInstructionsDialog(method);
                instructionsDialog.showAndWait();

                showSuccessAlert(
                        "Antecipação Confirmada",
                        "Fatura antecipada com sucesso via " + method.getDisplayName() + "!\n" +
                        "Seu limite foi liberado imediatamente."
                );

                loadInvoiceTimeline();
            } else {
                showErrorAlert("Erro na Antecipação", response.getMessage());
            }
        } catch (Exception e) {
            showErrorAlert("Erro", "Erro ao processar antecipação: " + e.getMessage());
            System.err.println("Error processing anticipation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProtectCard() {
        if (userId == null || creditCardId == null) {
            showErrorAlert("Erro", "Informações do cartão não disponíveis.");
            return;
        }

        int cardIndex = findCardIndex(creditCardId);
        if (cardIndex < 0 || userCards == null || cardIndex >= userCards.size()) {
            showErrorAlert("Erro", "Cartão não encontrado.");
            return;
        }

        CreditCardDto currentCard = userCards.get(cardIndex);

        CardSecurityContext context = new CardSecurityContext(
                userId,
                currentCard.getCardNumber(),
                currentCard.getMaskCardNumber(),
                currentCard.getStatus()
        );

        ViewSwitcher.switchTo(CreditCardView.CARD_SECURITY.getView(), context);
    }

    @FXML
    private void handleBarcode() {
        System.out.println("Show barcode");
    }
}
