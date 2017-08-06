package com.your.time.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.your.time.activity.ConsumerAppointmentActivity;
import com.your.time.activity.ConsumerHomeActivity;
import com.your.time.activity.IspHomeActivity;
import com.your.time.activity.IspSettingActivity;
import com.your.time.activity.MainActivity;
import com.your.time.activity.MapsActivity;
import com.your.time.activity.R;
import com.your.time.activity.SignUpActivity;
import com.your.time.activity.YourTimeActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Boscosiva on 22-07-2017.
 */

public class YourTimeUtil {

    public static void controlMenuShowHide(Activity activity,Menu menu,int itemIdToHide, Pages callingFrom){
        activity.getMenuInflater().inflate(R.menu.menus,menu);
        if(callingFrom.name().startsWith("ISP_")) {
            menu.setGroupVisible(R.id.menu_group_isp, true);
            menu.setGroupVisible(R.id.menu_group_consumer,false);
        }else {
            menu.setGroupVisible(R.id.menu_group_isp, false);
            menu.setGroupVisible(R.id.menu_group_consumer,true);
        }
        menu.findItem(itemIdToHide).setVisible(false);
    }

    public static void triggerMenuItemSelection(final Activity callingActivity, int menuItemId, final Pages currentActivity, final SessionManager sessionManager){
        Intent intent = null;
        switch (menuItemId){
            case R.id.consumer_action_home:
                intent = new Intent(callingActivity, ConsumerHomeActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.CONSUMER_HOME_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.consumer_action_profile:
                intent = new Intent(callingActivity, SignUpActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.CONSUMER_PROFILE_UPDATE_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.consumer_action_appointment:
                intent = new Intent(callingActivity, ConsumerAppointmentActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.consumer_action_book:
                intent = new Intent(callingActivity, MapsActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.CONSUMER_APPOINTMENT_ADD_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.consumer_action_history:
                intent = new Intent(callingActivity, ConsumerAppointmentActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.CONSUMER_APPOINTMENT_HISTORY_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.consumer_action_settings:
                intent = new Intent(callingActivity, IspSettingActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.CONSUMER_SETTING_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.consumer_action_logout:
                if(callingActivity instanceof YourTimeActivity) {
                    new AlertDialog.Builder(callingActivity)
                            .setTitle(callingActivity.getString(R.string.your_time_says))
                            .setMessage(callingActivity.getString(R.string.want_logout_question))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    sessionManager.logoutUser(callingActivity);
                                    Intent intent = new Intent((Context) callingActivity, MainActivity.class);
                                    intent.putExtra(((Context) callingActivity).getResources().getString(R.string.caller), currentActivity);
                                    intent.putExtra(((Context) callingActivity).getResources().getString(R.string.actAs), Pages.MAIN_ACTIVITY);
                                    callingActivity.startActivity(intent);
                                    callingActivity.finish();
                                }
                            }).setNegativeButton(android.R.string.no, null).show();
                }else{
                    sessionManager.logoutUser(callingActivity);
                }
                break;
            case R.id.isp_action_home:
                intent = new Intent(callingActivity, IspHomeActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.ISP_HOME_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.isp_action_profile:
                intent = new Intent(callingActivity, SignUpActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.ISP_PROFILE_UPDATE_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.isp_action_schedule:
                intent = new Intent(callingActivity, ConsumerAppointmentActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.ISP_ACTIVE_SCHEDULE_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.isp_action_add_schedule:
                intent = new Intent(callingActivity, MapsActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.ISP_SCHEDULE_ADD_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.isp_action_history:
                intent = new Intent(callingActivity, MapsActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.ISP_SCHEDULE_HISTORY_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.isp_action_settings:
                intent = new Intent(callingActivity, IspSettingActivity.class);
                intent.putExtra(callingActivity.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(callingActivity.getResources().getString(R.string.actAs), Pages.ISP_SETTING_ACTIVITY);
                callingActivity.startActivity(intent);
                callingActivity.finish();
                break;
            case R.id.isp_action_logout:
                if(callingActivity instanceof YourTimeActivity) {
                    //YourTimeUtil.dialog(callingActivity,callingActivity.getString(R.string.your_time_says),callingActivity.getString(R.string.want_logout_question),android.R.drawable.ic_input_get);
                    new AlertDialog.Builder(callingActivity)
                            .setTitle(callingActivity.getString(R.string.your_time_says))
                            .setMessage(R.string.want_logout_question)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((YourTimeActivity) callingActivity).getSessionManager().logoutUser((Context) callingActivity);
                                    Intent intent = new Intent((Context) callingActivity, MainActivity.class);
                                    intent.putExtra(((Context) callingActivity).getResources().getString(R.string.caller), ((YourTimeActivity) callingActivity).getCurrentActivity());
                                    intent.putExtra(((Context) callingActivity).getResources().getString(R.string.actAs), Pages.MAIN_ACTIVITY);
                                    callingActivity.startActivity(intent);
                                    callingActivity.finish();
                                }
                            }).setNegativeButton(android.R.string.no, null).show();
                }else{
                    //(callingActivity.logout());
                }
                break;
        }
    }

    public static void dialog(Activity activity,String title,String desc, int imageResourceId){
        // Create custom dialog object
        final Dialog dialog = new Dialog(activity);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle(title);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText(desc);
        if(imageResourceId == android.R.drawable.ic_dialog_info){
            if(activity.findViewById(R.id.rescheduleButton) != null)
                activity.findViewById(R.id.rescheduleButton).setVisibility(View.INVISIBLE);
            if(activity.findViewById(R.id.cancelButton) != null)
                activity.findViewById(R.id.cancelButton).setVisibility(View.INVISIBLE);
            if(activity.findViewById(R.id.okayButton) != null)
                activity.findViewById(R.id.okayButton).setVisibility(View.VISIBLE);
        }else{
            if(activity.findViewById(R.id.rescheduleButton) != null)
                activity.findViewById(R.id.rescheduleButton).setVisibility(View.VISIBLE);
            if(activity.findViewById(R.id.cancelButton) != null)
                activity.findViewById(R.id.cancelButton).setVisibility(View.VISIBLE);
            if(activity.findViewById(R.id.okayButton) != null)
                activity.findViewById(R.id.okayButton).setVisibility(View.INVISIBLE);
        }
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        image.setImageResource(imageResourceId);

        dialog.show();
    }

    public static CountDownTimer getTimer(final TextView textView, String waitTime){
        long milliseconds = 1000;
        if(waitTime != null) {
            String days = "0";
            int iDays = waitTime.indexOf("d");
            if(iDays != -1){
                days = waitTime.substring(0,iDays);
            }else{
                iDays = 0;
            }
            String hours = "0";
            int iHours = waitTime.indexOf("h");
            if(iHours != -1){
                hours = waitTime.substring(iDays==0?0:iDays+1,iHours);
            }else {
                iHours = 0;
            }
            String mins = "0";
            int iMins = waitTime.indexOf("m");
            if(iMins != -1){
                mins = waitTime.substring(iHours==0?0:iHours+1,iMins);
            }else{
                iMins = 0;
            }
            String secs = "0";
            int iSecs = waitTime.indexOf("s");
            if(iSecs != -1){
                secs = waitTime.substring(iMins==0?0:iMins+1,iSecs);
            }
            milliseconds = (Long.parseLong(days)* 1000 * 60 * 60 * 24)+(Long.parseLong(hours)* 1000 * 60 * 60)+(Long.parseLong(mins)* 1000 * 60)+(Long.parseLong(secs)* 1000);
        }
        return new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText(showWaitTime(millisUntilFinished / 1000));
            }

            public void onFinish() {
                textView.setText("DONE!");
            }
        };
    }

    public static String calculateWaitTime(String targetDate){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String waitTime = "";
        if(targetDate != null) {
            try {
                Date d1 = format.parse(targetDate);
                long diff = d1.getTime() - (new Date()).getTime();
                diff = diff < 0 ? 0 : diff;
                long days = Math.round(diff / 1000 / 60 / 60 / 24);
                long reminder = diff % (1000 * 60 * 60 * 24);
                long hours = reminder / 1000 / 60 / 60;
                reminder = reminder % (1000 * 60 * 60);
                long minutes = reminder / 1000 / 60;
                reminder = reminder % (1000 * 60);
                long seconds = reminder / 1000;
                if (days != 0)
                    waitTime += days + "d";
                if (hours != 0)
                    waitTime += hours + "h";
                if (minutes != 0)
                    waitTime += minutes + "m";
                if (seconds != 0)
                    waitTime += seconds + "s";
                System.out.println("Waiting Time : " + waitTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return waitTime;
    }

    public static String showWaitTime(long milliSeconds){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        //return format.format(new Date(milliSeconds));
        //return ""+milliSeconds;
        DateFormat dateFormat = DateFormat.getTimeInstance();
        return dateFormat.format(new Date(milliSeconds));
    }
}
