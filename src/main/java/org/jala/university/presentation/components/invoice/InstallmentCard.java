package org.jala.university.presentation.components.invoice;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.jala.university.application.dto.PurchaseInstallmentDto;
import org.jala.university.presentation.utils.CategoryIconProvider;
import org.jala.university.presentation.utils.CurrencyFormatter;
import org.jala.university.presentation.utils.DateFormatter;

public class InstallmentCard extends VBox {

    private final PurchaseInstallmentDto installment;

    public InstallmentCard(PurchaseInstallmentDto installment) {
        this.installment = installment;
        buildCard();
    }

    private void buildCard() {
        setSpacing(8);
        getStyleClass().add("card");
        setStyle("-fx-padding: 16; -fx-cursor: hand;");

        HBox header = createHeader();
        VBox.setVgrow(header, Priority.NEVER);

        HBox details = createDetails();
        VBox.setVgrow(details, Priority.NEVER);

        getChildren().addAll(header, details);
    }

    private HBox createHeader() {
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setGlyphName(CategoryIconProvider.getIconName(installment.getCategory()));
        icon.setSize("24");
        icon.setFill(Paint.valueOf("#1565C0"));

        VBox merchantInfo = createMerchantInfo();
        HBox.setHgrow(merchantInfo, Priority.ALWAYS);

        Label amountLabel = new Label(CurrencyFormatter.format(installment.getInstallmentAmount()));
        amountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #212121;");

        header.getChildren().addAll(icon, merchantInfo, amountLabel);
        return header;
    }

    private VBox createMerchantInfo() {
        VBox merchantInfo = new VBox(2);

        Label merchantLabel = new Label(installment.getMerchant() != null ? installment.getMerchant() : "Compra");
        merchantLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 600; -fx-text-fill: #212121;");

        merchantInfo.getChildren().add(merchantLabel);

        if (installment.getPurchaseDate() != null) {
            Label dateLabel = new Label(DateFormatter.formatShortDate(installment.getPurchaseDate()));
            dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");
            merchantInfo.getChildren().add(dateLabel);
        }

        return merchantInfo;
    }

    private HBox createDetails() {
        HBox details = new HBox();
        details.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(details, Priority.ALWAYS);

        String installmentInfo = installment.getTotalInstallments() > 1 ?
                String.format("Parcela %d/%d - Total: %s",
                        installment.getInstallmentNumber(),
                        installment.getTotalInstallments(),
                        CurrencyFormatter.format(installment.getTotalAmount())) :
                "À vista";

        Label installmentLabel = new Label(installmentInfo);
        installmentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #616161;");

        HBox.setHgrow(installmentLabel, Priority.ALWAYS);

        details.getChildren().add(installmentLabel);
        return details;
    }

    public PurchaseInstallmentDto getInstallment() {
        return installment;
    }
}
