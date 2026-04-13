package seedu.crypto1010.model;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Normalizes and validates wallet currency codes.
 */
public final class CurrencyCode {
    public static final String GENERIC = "generic";
    private static final Pattern SPECIFIC_CURRENCY_PATTERN = Pattern.compile("^[A-Za-z0-9]{2,10}$");

    private CurrencyCode() {
    }

    public static String normalize(String currencyCode) {
        return currencyCode == null ? "" : currencyCode.trim().toLowerCase(Locale.ROOT);
    }

    public static String normalizeOrDefault(String currencyCode) {
        String normalized = normalize(currencyCode);
        return normalized.isEmpty() ? GENERIC : normalized;
    }

    public static boolean isValidSpecificCurrency(String currencyCode) {
        return SPECIFIC_CURRENCY_PATTERN.matcher(normalize(currencyCode)).matches();
    }

    public static boolean isGeneric(String currencyCode) {
        return GENERIC.equals(normalizeOrDefault(currencyCode));
    }
}
