package com.nimblix.SchoolPEPProject.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SchoolUtil {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static LocalDateTime changeCurrentTimeToLocalDateTimeFromGmtToIST() {
        LocalDateTime gmtTime = LocalDateTime.now(ZoneOffset.UTC);
        ZoneId istZone = ZoneId.of("Asia/Kolkata");
        LocalDateTime istTime = gmtTime.atZone(ZoneOffset.UTC).withZoneSameInstant(istZone).toLocalDateTime();

        return istTime;
    }

    public static String changeCurrentTimeToLocalDateFromGmtToISTInString() {
        LocalDateTime gmtTime = LocalDateTime.now(ZoneOffset.UTC);
        ZoneId istZone = ZoneId.of("Asia/Kolkata");
        LocalDateTime istTime = gmtTime.atZone(ZoneOffset.UTC).withZoneSameInstant(istZone).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return istTime.format(formatter);
    }

    public static String getExpiryTimeInISTString(int minutesToAdd) {
        LocalDateTime gmtTime = LocalDateTime.now(ZoneOffset.UTC);
        ZoneId istZone = ZoneId.of("Asia/Kolkata");

        // Convert GMT to IST
        LocalDateTime istTime = gmtTime.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(istZone)
                .toLocalDateTime();

        // Add minutes for expiry
        istTime = istTime.plusMinutes(minutesToAdd);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return istTime.format(formatter);
    }

    public static String nowIST() {
        return LocalDateTime
                .now(ZoneId.of("Asia/Kolkata"))
                .format(FORMATTER);
    }

    public static String plusDaysIST(int days) {
        return LocalDateTime
                .now(ZoneId.of("Asia/Kolkata"))
                .plusDays(days)
                .format(FORMATTER);
    }

    public static long daysRemaining(String endDate) {
        LocalDateTime end =
                LocalDateTime.parse(endDate, FORMATTER);
        LocalDateTime now =
                LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        return ChronoUnit.DAYS.between(now, end);
    }


}
