package util;

import java.util.Locale;

public final class MoneyFormat {
    private MoneyFormat() {
    }
    public static String format(long amount) {
        return String.format(Locale.US, "%,d so'm", amount);
    }
}