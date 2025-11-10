package org.cityu.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
    /**
     * Get current date
     * @return Date
     */
    public static Date getCurrentDate() {
        Date currentDate = new Date();
        return currentDate;
    }

    /**
     * Generate invoice number
     * @param series increase series
     * @return invoice number
     */
    public static String generateInvoiceNumber(int series) {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = formatter.format(getCurrentDate());
        stringBuilder.append(formattedDate);
        String seriesString = String.valueOf(series);
        for (int i = 0; i < 8 - seriesString.length(); i++) {
            stringBuilder.append("0");
        }
        stringBuilder.append(seriesString);
        return stringBuilder.toString();
    }

    /**
     * Convert date from String to Date
     * @param date String
     * @return Date
     * @throws ParseException
     */
    public static Date formatDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(date);
    }

    public static String generateApplicationFormNumber(int series) {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = formatter.format(getCurrentDate());
        stringBuilder.append(formattedDate);
        String seriesString = String.valueOf(series);
        for (int i = 0; i < 6 - seriesString.length(); i++) {
            stringBuilder.append("0");
        }
        stringBuilder.append(seriesString);
        return stringBuilder.toString();
    }

}
