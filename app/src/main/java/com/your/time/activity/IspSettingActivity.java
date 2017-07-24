package com.your.time.activity;

import android.content.Intent;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.isp_toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.isp_fab);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_isp_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = null;
        switch (id){
            case R.id.isp_action_setting_home:
                intent = new Intent(this, IspHomeActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.ISP_SETTING_ACTIVITY);
                startActivity(intent);
                finish();
                break;
            case R.id.isp_action_setting_logout:
                callingFrom = Pages.ISP_SETTING_ACTIVITY;
                super.logout(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
