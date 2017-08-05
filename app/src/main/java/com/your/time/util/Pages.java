package com.your.time.util;

/**
 * Created by Boscosiva on 22-07-2017.
 */

public enum Pages {
    /** Common Activities **/
    //No login required
    MAIN_ACTIVITY,
    SIGN_UP_ACTIVITY,
    LOGIN_ACTIVITY,
    MAPS_ACTIVITY,

    //login required
    FEEDBACK_ACTIVITY,
    BOOK_ACTIVITY,

    /** Consumer Specific activity **/
    CONSUMER_HOME_ACTIVITY,
    CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY,
    CONSUMER_APPOINTMENT_HISTORY_ACTIVITY,
    CONSUMER_APPOINTMENT_ADD_ACTIVITY,
    CONSUMER_APPOINTMENT_UPDATE_ACTIVITY,
    CONSUMER_PROFILE_UPDATE_ACTIVITY,
    CONSUMER_SETTING_ACTIVITY,

    /** ISP Specific Activity **/
    ISP_HOME_ACTIVITY,
    ISP_ACTIVE_SCHEDULE_ACTIVITY,
    ISP_SCHEDULE_HISTORY_ACTIVITY,
    ISP_SCHEDULE_ADD_ACTIVITY,
    ISP_SCHEDULE_UPDATE_ACTIVITY,
    ISP_PROFILE_UPDATE_ACTIVITY,
    ISP_SETTING_ACTIVITY;

    public static boolean isCommonNoLoginRequired(Pages activityType){
        return (activityType == MAIN_ACTIVITY
                || activityType == SIGN_UP_ACTIVITY
                || activityType == LOGIN_ACTIVITY
                || activityType == MAPS_ACTIVITY
                || activityType == FEEDBACK_ACTIVITY
                || activityType == BOOK_ACTIVITY)?true:false;
    }

    public static boolean isCommonLoginRequired(Pages activityType){
        return (activityType == MAPS_ACTIVITY
                || activityType == FEEDBACK_ACTIVITY
                || activityType == BOOK_ACTIVITY)?true:false;
    }

    public static boolean isConsumerSpecific(Pages activityType,boolean checkLogin){
        boolean isConsumerSpecific = (   activityType == CONSUMER_HOME_ACTIVITY
                                    || activityType == CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY
                                    || activityType == CONSUMER_APPOINTMENT_HISTORY_ACTIVITY
                                    || activityType == CONSUMER_APPOINTMENT_ADD_ACTIVITY
                                    || activityType == CONSUMER_APPOINTMENT_UPDATE_ACTIVITY
                                    || activityType == CONSUMER_PROFILE_UPDATE_ACTIVITY
                                    || activityType == CONSUMER_SETTING_ACTIVITY)?true:false;
        boolean loginCheck = false;
        if(checkLogin)
            loginCheck = isCommonLoginRequired(activityType);
        else
            loginCheck = isCommonNoLoginRequired(activityType);
        return isConsumerSpecific && loginCheck;
    }

    public static boolean isIspSpecific(Pages activityType,boolean checkLogin){
        boolean isIspSpecific = (   activityType == ISP_HOME_ACTIVITY
                || activityType == ISP_ACTIVE_SCHEDULE_ACTIVITY
                || activityType == ISP_SCHEDULE_HISTORY_ACTIVITY
                || activityType == ISP_SCHEDULE_ADD_ACTIVITY
                || activityType == ISP_SCHEDULE_UPDATE_ACTIVITY
                || activityType == ISP_PROFILE_UPDATE_ACTIVITY
                || activityType == ISP_SETTING_ACTIVITY)?true:false;
        boolean loginCheck = false;
        if(checkLogin)
            loginCheck = isCommonLoginRequired(activityType);
        else
            loginCheck = isCommonNoLoginRequired(activityType);
        return isIspSpecific && loginCheck;
    }
}
