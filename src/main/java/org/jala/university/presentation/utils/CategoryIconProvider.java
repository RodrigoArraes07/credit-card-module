package org.jala.university.presentation.utils;

import org.jala.university.domain.enums.PurchaseCategory;

public class CategoryIconProvider {

    private CategoryIconProvider() {
    }

    public static String getIconName(PurchaseCategory category) {
        if (category == null) return "SHOPPING_CART";
        return switch (category) {
            case FOOD -> "CUTLERY";
            case TRANSPORT -> "CAR";
            case HEALTH -> "HEART";
            case EDUCATION -> "BOOK";
            case ENTERTAINMENT -> "PLAY_CIRCLE";
            case OTHER -> "SHOPPING_CART";
            default -> "SHOPPING_CART";
        };
    }
}
