package org.jala.university.presentation.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private CurrencyFormatter() {
    }

    public static String format(Double value) {
        if (value == null) return "R$ 0,00";
        return FORMATTER.format(value);
    }

    public static String format(double value) {
        return FORMATTER.format(value);
    }
}
