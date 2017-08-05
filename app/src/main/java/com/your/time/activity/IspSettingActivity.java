package com.your.time.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.your.time.bean.HeaderInfo;
import com.your.time.util.Pages;
import com.your.time.util.YourTimeUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class IspSettingActivity extends YourTimeActivity implements RestCaller{

    private static final String TAG = "IspSettingActivity";
    List<HeaderInfo> myServiceTypes = new ArrayList<HeaderInfo>();
    private String selectedServiceType = null;
    private static String currentCaller = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentActivity = Pages.SIGN_UP_ACTIVITY;
        activity = this;
        super.onCreate(savedInstanceState);
    }
    public void loadUI(){
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.signup_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);
    }

    @Override
    public void onWebServiceResult(JSONObject jsonObject) {
        Log.i(TAG,jsonObject.toString());
        if(currentCaller == null)return;
        else {

        }
        currentCaller = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        YourTimeUtil.controlMenuShowHide(this,menu,R.id.isp_action_settings,currentActivity);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        YourTimeUtil.triggerMenuItemSelection(this,item.getItemId(),currentActivity,getSessionManager());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean updateView() {
        return false;
    }

    @Override
    public boolean updateModel() {
        return false;
    }
}
