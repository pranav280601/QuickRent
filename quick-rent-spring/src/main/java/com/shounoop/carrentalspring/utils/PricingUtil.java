package com.shounoop.carrentalspring.utils;


import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Small utility to check peak times and apply multipliers.
 */
public class PricingUtil {

    public static boolean isPeakTime(LocalDateTime dt) {
        DayOfWeek d = dt.getDayOfWeek();
        int hour = dt.getHour();
        if (d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY) return true;
        if ((hour >= 6 && hour <= 9) || (hour >= 17 && hour <= 21)) return true;
        return false;
    }

    public static double applyPeakMultiplier(double amount, LocalDateTime dt) {
        return isPeakTime(dt) ? amount * 1.25 : amount;
    }
}
