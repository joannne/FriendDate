package com.joanne.frienddate;

import android.app.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class global extends Application {

    public static String username = null;

    // convert date format to MM/dd/yy
    public static String parseDate(String input) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yy");
        Date date;
        String str = null;
        try {
            date = inputFormat.parse(input);
            str = outputFormat.format(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
