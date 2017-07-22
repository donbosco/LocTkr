package com.your.time.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Boscosiva on 22-07-2017.
 */

public class YourTimeUtil {
    public String calculateWaitTime(String targetDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String waitTime = "";
        try {
            Date d1 = format.parse(targetDate);
            long diff = d1.getTime() - (new Date()).getTime();
            long days = diff/(1000 * 60 * 60 * 24);
            long hours = (diff-(1000 * 60 * 60 * 24))/(1000 * 60 * 60);
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            System.out.println("Time in seconds: " + diffSeconds + " seconds.");
            System.out.println("Time in minutes: " + diffMinutes + " minutes.");
            System.out.println("Time in hours: " + diffHours + " hours.");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
