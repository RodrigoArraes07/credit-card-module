package org.jala.university.presentation.components;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.jala.university.application.dto.PurchaseDto;
import org.jala.university.domain.enums.PurchaseCategory;
import org.jala.university.presentation.utils.CategoryIconProvider;
import org.jala.university.presentation.utils.CurrencyFormatter;
import org.jala.university.presentation.utils.DateFormatter;

public class TransactionCard extends VBox {

    private final PurchaseDto purchase;

    public TransactionCard(PurchaseDto purchase) {
        this.purchase = purchase;
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
        PurchaseCategory category = purchase.getCategory() != null ?
                PurchaseCategory.valueOf(purchase.getCategory()) : null;
        icon.setGlyphName(CategoryIconProvider.getIconName(category));
        icon.setSize("24");
        icon.setFill(Paint.valueOf("#1565C0"));

        VBox merchantInfo = createMerchantInfo();
        HBox.setHgrow(merchantInfo, Priority.ALWAYS);

        Label amountLabel = new Label(CurrencyFormatter.format(purchase.getTotalAmount()));
        amountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #212121;");

        header.getChildren().addAll(icon, merchantInfo, amountLabel);
        return header;
    }

    private VBox createMerchantInfo() {
        VBox merchantInfo = new VBox(2);

        Label merchantLabel = new Label(purchase.getMerchant() != null ? purchase.getMerchant() : "Compra");
        merchantLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 600; -fx-text-fill: #212121;");

        merchantInfo.getChildren().add(merchantLabel);

        if (purchase.getPurchaseDate() != null) {
            Label dateLabel = new Label(DateFormatter.formatShortDate(purchase.getPurchaseDate()));
            dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");
            merchantInfo.getChildren().add(dateLabel);
        }

        return merchantInfo;
    }

    private HBox createDetails() {
        HBox details = new HBox();
        details.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(details, Priority.ALWAYS);

        String installmentInfo = purchase.getTotalInstallments() > 1 ?
                String.format("Compra em %dx de %s",
                        purchase.getTotalInstallments(),
                        CurrencyFormatter.format(purchase.getTotalAmount() / purchase.getTotalInstallments())) :
                "À vista";

        Label installmentLabel = new Label(installmentInfo);
        installmentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #616161;");

        HBox.setHgrow(installmentLabel, Priority.ALWAYS);

        details.getChildren().add(installmentLabel);
        return details;
    }

    public PurchaseDto getPurchase() {
        return purchase;
    }
}
