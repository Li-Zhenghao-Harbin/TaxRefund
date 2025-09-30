package org.cityu.common.utils;

import java.text.SimpleDateFormat;
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
}
