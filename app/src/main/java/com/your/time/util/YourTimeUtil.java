package com.your.time.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.your.time.activity.ConsumerAppointmentActivity;
import com.your.time.activity.ConsumerHomeActivity;
import com.your.time.activity.IspHomeActivity;
import com.your.time.activity.IspSettingActivity;
import com.your.time.activity.MapsActivity;
import com.your.time.activity.R;
import com.your.time.activity.SignUpActivity;
import com.your.time.activity.YourTimeActivity;

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
            case R.id.isp_action_logout:
                ((YourTimeActivity) callingActivity).logout();
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
        }
    }

    public static Dialog dialog(Activity activity,String title,String desc, int imageResourceId){
        // Create custom dialog object
        final Dialog dialog = new Dialog(activity);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle(title);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText(desc);
        if(imageResourceId == R.drawable.ic_info){
            if(dialog.findViewById(R.id.rescheduleButton) != null)
                dialog.findViewById(R.id.rescheduleButton).setVisibility(View.GONE);
            if(dialog.findViewById(R.id.cancelButton) != null)
                dialog.findViewById(R.id.cancelButton).setVisibility(View.GONE);
            if(dialog.findViewById(R.id.okayButton) != null)
                dialog.findViewById(R.id.okayButton).setVisibility(View.VISIBLE);
        }else{
            if(dialog.findViewById(R.id.rescheduleButton) != null)
                dialog.findViewById(R.id.rescheduleButton).setVisibility(View.VISIBLE);
            if(dialog.findViewById(R.id.cancelButton) != null)
                dialog.findViewById(R.id.cancelButton).setVisibility(View.VISIBLE);
            if(dialog.findViewById(R.id.okayButton) != null)
                dialog.findViewById(R.id.okayButton).setVisibility(View.GONE);
        }
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        image.setImageResource(imageResourceId);
        image.setAdjustViewBounds(true);
        image.getLayoutParams().height = 100;
        image.getLayoutParams().width = 100;

        dialog.show();
        return dialog;
    }

    public static CountDownTimer getTimer(final TextView textView, final String waitTime){

        final int pdays;
        final int phours;
        final int pmins;
        final int psecs;
        long milliseconds = 1000;
        if(waitTime != null) {
            String sDays = "0";
            int iDays = waitTime.indexOf("d");
            if(iDays != -1){
                sDays = waitTime.substring(0,iDays);
            }else{
                iDays = 0;
            }
            String sHours = "0";
            int iHours = waitTime.indexOf("h");
            if(iHours != -1){
                sHours = waitTime.substring(iDays==0?0:iDays+1,iHours);
            }else {
                iHours = 0;
            }
            String sMins = "0";
            int iMins = waitTime.indexOf("m");
            if(iMins != -1){
                sMins = waitTime.substring(iHours==0?0:iHours+1,iMins);
            }else{
                iMins = 0;
            }
            String sSecs = "0";
            int iSecs = waitTime.indexOf("s");
            if(iSecs != -1){
                sSecs = waitTime.substring(iMins==0?0:iMins+1,iSecs);
            }
            pdays = Integer.parseInt(sDays);
            phours = Integer.parseInt(sHours);
            pmins = Integer.parseInt(sMins);
            psecs = Integer.parseInt(sSecs);
            milliseconds = (pdays* 1000 * 60 * 60 * 24)+(phours* 1000 * 60 * 60)+(pmins* 1000 * 60)+(psecs* 1000);
            return new CountDownTimer(milliseconds, 1000) {
                int days = pdays;
                int hours = phours;
                int mins = pmins;
                int secs = psecs;
                public void onTick(long millisUntilFinished) {
                    if (secs <= 0) {
                        if(mins <= 0) {
                            if (hours <= 0) {
                                if(days <= 0){

                                }else {
                                    days = days - 1;
                                    hours = 23;
                                    mins = 59;
                                    secs = 60;
                                }
                            }else{
                                hours = hours -1;
                                mins = 59;
                                secs = 60;
                            }
                        }else{
                            mins = mins -1;
                            secs = 60;
                        }
                    }
                    secs = secs - 1;
                    String waitingTimeText = (days>0?((days<10?"0"+days:days)+":"):"")+(hours>0?((hours<10?"0"+hours:hours)+":"):"")+(mins>0?((mins<10?"0"+mins:mins)+":"):"")+(secs<10?"0"+secs:secs);
                    textView.setText(waitingTimeText);
                }

                public void onFinish() {
                    textView.setText("DONE!");
                }
            };
        }else
            return new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    textView.setText("DONE!");
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
}
