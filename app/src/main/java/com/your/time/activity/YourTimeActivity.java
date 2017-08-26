package com.your.time.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.your.time.util.Pages;
import com.your.time.util.SessionManager;
import com.your.time.util.YourTimeUtil;

/**
 * Created by Boscosiva on 22-07-2017.
 */

public abstract class YourTimeActivity extends AppCompatActivity {
    private SessionManager SESSION_MANAGER;
    protected Pages currentActivity;
    protected Activity activity;
    protected Pages callingFrom;
    protected ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getIntent().getExtras() != null) {
            callingFrom = (Pages) this.getIntent().getExtras().get(this.getResources().getString(R.string.caller));
            currentActivity = (Pages) this.getIntent().getExtras().get(this.getResources().getString(R.string.actAs));
        }
        SESSION_MANAGER = new SessionManager(getApplicationContext());
        if(!SESSION_MANAGER.isLoggedIn() && Pages.isCommonLoginRequired(currentActivity)){
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra(this.getResources().getString(R.string.caller), Pages.MAIN_ACTIVITY);
            intent.putExtra(this.getResources().getString(R.string.actAs), Pages.MAIN_ACTIVITY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
        }else{
            loadUI();
        }
    }

    public abstract void loadUI();

    public void logout(final Context context){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.your_time_says))
                .setMessage(getString(R.string.want_logout_question))
                .setIcon(R.drawable.alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        SESSION_MANAGER.logoutUser(context);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra(context.getResources().getString(R.string.caller), callingFrom);
                        intent.putExtra(context.getResources().getString(R.string.actAs), Pages.MAIN_ACTIVITY);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    public SessionManager getSessionManager() {
        return SESSION_MANAGER;
    }

    public Pages getCurrentActivity(){
        return currentActivity;
    }

    public abstract boolean updateView();

    public abstract boolean updateModel();

    public void dialogClose(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void okayClicked(View view){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void logoutClicked(View view){
        getSessionManager().logoutUser(YourTimeActivity.this);
        Intent intent = new Intent(YourTimeActivity.this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.caller), currentActivity);
        intent.putExtra(getResources().getString(R.string.actAs), Pages.MAIN_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public void logout(){
        dialog = YourTimeUtil.dialog(this,getString(R.string.your_time_says),getString(R.string.want_logout_question),R.drawable.logout);
        /*new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.your_time_says))
                .setMessage(this.getString(R.string.want_logout_question))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getSessionManager().logoutUser(YourTimeActivity.this);
                        Intent intent = new Intent(YourTimeActivity.this, MainActivity.class);
                        intent.putExtra(getResources().getString(R.string.caller), currentActivity);
                        intent.putExtra(getResources().getString(R.string.actAs), Pages.MAIN_ACTIVITY);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton(android.R.string.no, null).show();*/

    }
}
