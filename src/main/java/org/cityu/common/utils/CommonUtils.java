package org.cityu.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class CommonUtils {

    public static Date getCurrentDate() {
        Date currentDate = new Date();
        return currentDate;
    }

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

    public static String getStringFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
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

    public static String generateApplicationFormMaterial(String applicationFormNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("xxx/applicationForm/")
                .append(applicationFormNumber)
                .append(".pdf");
        return stringBuilder.toString();
    }

    public static String generateInvoiceMaterial(String applicationFormNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("xxx/invoice/")
                .append(applicationFormNumber) // invoice related application form number
                .append(".pdf");
        return stringBuilder.toString();
    }

    public static String encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String decode(String str) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(str);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
