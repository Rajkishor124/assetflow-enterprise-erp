package com.assetflow.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateUtils {
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private DateUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String toIsoString(Instant instant) {
        if (instant == null) return null;
        return ISO_FORMATTER.format(instant.atOffset(ZoneOffset.UTC));
    }
}
