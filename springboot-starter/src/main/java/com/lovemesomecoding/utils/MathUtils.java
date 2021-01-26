package com.lovemesomecoding.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MathUtils {

    public static double getProratedAmountByDay(Date startDate, Date endDate, double monthlyCost) {

        if (startDate == null) {
            log.info("startDate is null => 0 propration");
            return 0;
        }
        if (endDate == null) {
            log.info("endDate is null => 0 propration");
            return 0;
        }

        LocalDate utcStartDate = startDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();
        LocalDate utcEndDate = endDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();

        log.info("utcStartDate={},utcEndDate={},monthlyCost={}", utcStartDate.toString(), utcEndDate.toString(), monthlyCost);

        if (utcStartDate.isAfter(utcEndDate)) {
            log.info("startDate is after endDate which is invalid => 0 propration");
            return 0;
        }

        if (utcStartDate.isEqual(utcEndDate)) {
            log.info("startDate is equal => 0 propration");
            return 0;
        }

        double proratedAmount = 0;

        double costPerDay = 0;

        // 1 - 31
        int numberOfDaysInMonth = 0;

        // same year and same month
        if (utcStartDate.getYear() == utcEndDate.getYear() && utcStartDate.getMonthValue() == utcEndDate.getMonthValue()) {

            numberOfDaysInMonth = utcStartDate.lengthOfMonth();

            Period diff = Period.between(utcStartDate, utcEndDate);

            costPerDay = monthlyCost / numberOfDaysInMonth;

            int numberOfDaysToProrate = (diff.getDays() + 1);

            proratedAmount = BigDecimal.valueOf(numberOfDaysToProrate * costPerDay).setScale(2, RoundingMode.HALF_UP).doubleValue();

            log.info("monthlyCost={}, numberOfDaysToProrate={},costPerDay={}, numberOfDaysInMonth={}, proratedAmount={}", monthlyCost, diff.getDays(), costPerDay, numberOfDaysInMonth, proratedAmount);

            return proratedAmount;
        }

        LocalDate monthFirstDate = utcStartDate;
        LocalDate monthLastDate = LocalDate.of(monthFirstDate.getYear(), monthFirstDate.getMonthValue(), monthFirstDate.lengthOfMonth());

        int count = 0;

        do {

            Period diff = Period.between(monthFirstDate, monthLastDate);

            // add 1 to include the startDate
            int numberOfDaysToProrate = (diff.getDays() + 1);

            numberOfDaysInMonth = monthFirstDate.lengthOfMonth();

            double monthlyProratedAmount = 0;

            if (count == 0) {
                log.info("first month");

                costPerDay = monthlyCost / numberOfDaysInMonth;

                // add 1 to include the startDate
                monthlyProratedAmount = BigDecimal.valueOf(numberOfDaysToProrate * costPerDay).setScale(2, RoundingMode.HALF_UP).doubleValue();

            } else if (monthLastDate.isEqual(utcEndDate) || monthLastDate.isBefore(utcEndDate)) {
                log.info("regular month full refund");
                // refund whole month
                monthlyProratedAmount = monthlyCost;

            } else if (monthLastDate.isAfter(utcEndDate)) {
                log.info("last month");
                // last month
                diff = Period.between(monthFirstDate, utcEndDate);

                costPerDay = monthlyCost / numberOfDaysInMonth;

                numberOfDaysToProrate = (diff.getDays() + 1);

                // add 1 to include the startDate
                monthlyProratedAmount = BigDecimal.valueOf(numberOfDaysToProrate * costPerDay).setScale(2, RoundingMode.HALF_UP).doubleValue();

            }

            proratedAmount += monthlyProratedAmount;

            log.info("monthlyCost={}, monthFirstDate={}, monthLastDate={}, numberOfDaysToProrate={},costPerDay={}, numberOfDaysInMonth={},monthlyProratedAmount={}, proratedAmount={}", monthlyCost,
                    monthFirstDate, monthLastDate, numberOfDaysToProrate, costPerDay, numberOfDaysInMonth, monthlyProratedAmount, proratedAmount);

            monthFirstDate = monthLastDate.plusDays(1);
            monthLastDate = LocalDate.of(monthFirstDate.getYear(), monthFirstDate.getMonthValue(), monthFirstDate.lengthOfMonth());

            // log.info("monthFirstDate={},monthLastDate={}", monthFirstDate.toString(), monthLastDate.toString());

            count++;

        } while (monthFirstDate.getYear() <= utcEndDate.getYear() && monthFirstDate.getMonthValue() <= utcEndDate.getMonthValue());

        return proratedAmount;
    }

}
