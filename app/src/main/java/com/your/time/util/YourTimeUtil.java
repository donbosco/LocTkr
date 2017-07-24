package com.your.time.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Boscosiva on 22-07-2017.
 */

public class YourTimeUtil {
    public static String calculateWaitTime(String targetDate){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String waitTime = "";
        try {
            Date d1 = format.parse(targetDate);
            long diff = d1.getTime() - (new Date()).getTime();
            diff = diff < 0?0:diff;
            long days = Math.round(diff/1000/60/60/24);
            long reminder = diff % (1000*60*60*24);
            long hours = reminder/1000/60/60;
            reminder = reminder % (1000*60*60);
            long minutes = reminder/1000/60;
            reminder = reminder % (1000*60);
            long seconds = reminder/1000;
            if (days != 0)
                waitTime += days+"d";
            if (hours != 0)
                waitTime += hours+"h";
            if (minutes != 0)
                waitTime += minutes+"m";
            if (seconds != 0)
                waitTime += seconds+"s";
            System.out.println("Waiting Time : "+waitTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return waitTime;
    }
}
