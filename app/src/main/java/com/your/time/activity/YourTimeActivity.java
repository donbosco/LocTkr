package com.your.time.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.your.time.util.Pages;
import com.your.time.util.SessionManager;

/**
 * Created by Boscosiva on 22-07-2017.
 */

public abstract class YourTimeActivity extends AppCompatActivity {
    protected SessionManager SESSION_MANAGER;
    protected Pages currentActivity;
    protected Activity activity;
    protected Pages callingFrom;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getIntent().getExtras() != null) {
            callingFrom = (Pages) this.getIntent().getExtras().get(this.getResources().getString(R.string.caller));
        }
        /*SESSION_MANAGER = new SessionManager(getApplicationContext());
        if(!SESSION_MANAGER.isLoggedIn() && (currentActivity != null && Pages.LOGIN_ACTIVITY != currentActivity && Pages.SEARCH_ACTIVITY != currentActivity && Pages.MAIN_ACTIVITY != currentActivity)){
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra(this.getResources().getString(R.string.caller), Pages.MAIN_ACTIVITY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
        }else{*/
            loadUI();
        //}
    }

    public abstract void loadUI();
}
