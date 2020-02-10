package org.tigerdb.test.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class TimeUtil {

    public static NumberFormat getNumberFormat(String pattern) {
        DecimalFormatSymbols formatSymbols = DecimalFormatSymbols.getInstance();
        formatSymbols.setDecimalSeparator('.');

        return new DecimalFormat(pattern, formatSymbols);
    }

    public static double millisToSeconds(long millis) {
        NumberFormat numberFormat = getNumberFormat("#0.000");
        double seconds = ((double) millis) / Math.pow(10, 3);
        return Double.parseDouble(numberFormat.format(seconds));
    }

    public static double nanosToSeconds(long nanos) {
        NumberFormat numberFormat = getNumberFormat("#0.000");
        double seconds = ((double) nanos) / Math.pow(10, 9);
        return Double.parseDouble(numberFormat.format(seconds));
    }
}
