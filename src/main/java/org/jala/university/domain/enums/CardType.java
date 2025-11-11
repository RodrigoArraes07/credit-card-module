package org.jala.university.domain.enums;

import lombok.Getter;

@Getter
public enum CardType {
    GOLD("Gold Card", "Ideal for everyday purchases", "/assets/gold_slide.png", "#FFD700"),
    INTERNACIONAL("Internacional Card", "Shop worldwide with no fees", "/assets/internacional_slide.png", "#1976D2"),
    INFINITE("Infinite Card", "Unlimited benefits and rewards", "/assets/infinite_slide.png", "#000000"),
    PLATINUM("Platinum Card", "Premium perks and exclusive access", "/assets/platinum_slide.png", "#C0C0C0");

    private final String displayName;
    private final String description;
    private final String imagePath;
    private final String accentColor;

    CardType(String displayName, String description, String imagePath, String accentColor) {
        this.displayName = displayName;
        this.description = description;
        this.imagePath = imagePath;
        this.accentColor = accentColor;
    }
}
