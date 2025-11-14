package org.jala.university.presentation.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM.", PT_BR);
    private static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd 'de' MMMM", PT_BR);
    private static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", PT_BR);

    private DateFormatter() {
    }

    public static String formatMonth(LocalDate date) {
        if (date == null) return "-";
        return date.format(MONTH_FORMATTER);
    }

    public static String formatFullDate(LocalDate date) {
        if (date == null) return "-";
        return date.format(FULL_DATE_FORMATTER);
    }

    public static String formatShortDate(LocalDate date) {
        if (date == null) return "-";
        return date.format(SHORT_DATE_FORMATTER);
    }

    public static String formatShortDate(LocalDateTime dateTime) {
        if (dateTime == null) return "-";
        return dateTime.format(SHORT_DATE_FORMATTER);
    }
}
