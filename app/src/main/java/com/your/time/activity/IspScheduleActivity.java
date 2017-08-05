package com.your.time.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.your.time.bean.Booking;
import com.your.time.bean.User;
import com.your.time.custom.adapter.CommonArrayAdapter;
import com.your.time.util.Pages;
import com.your.time.util.ReflectionUtil;
import com.your.time.util.RestServiceHandler;
import com.your.time.util.YourTimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IspScheduleActivity extends YourTimeActivity implements RestCaller{

    ListView grid;

    private static final String TAG = "IspHomeActivity";
    private static String currentCaller = null;
    private List<User> users = new ArrayList<User>();
    private Booking booking;
    private List<Booking> bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentActivity = Pages.ISP_HOME_ACTIVITY;
        activity = this;
        super.onCreate(savedInstanceState);
    }
    public void loadUI(){
        setContentView(R.layout.activity_isp_schedule);
        grid =(ListView) findViewById(R.id.isp_schedule_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.isp_schedule_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.isp_schedule_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*final List<User> users = new ArrayList<User>();
        users.add(new User(null, "Don", null, null, null, null,null, null, null, null, null, null,"846546874", false, null));
        users.add(new User(null, "Bosco", null, null, null, null,null, null, null, null, null, null,"846546874", false, null));
        users.add(new User(null, "Rayappan", null, null, null, null,null, null, null, null, null, null,"846546874", false, null));

        Status<User> status = new Status<User>();
        status.setResults(users);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String string = objectMapper.writeValueAsString(status.getResults());
            status = null;
            List<User> l = objectMapper.readValue(string, TypeFactory.defaultInstance().constructCollectionType(List.class, User.class));
            for (User user :
                    l) {
                Log.i(TAG,user.getUsername());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        loadSchedules();
    }

    private void loadSchedules() {
        Map<String, Object> params = new HashMap<String,Object>();
        Booking booking = new Booking();
        booking.setUsername(getSessionManager().getUserDetails().getUsername());
        params.put(this.getResources().getString(R.string.ws_param),booking);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        if(currentActivity.equals(Pages.ISP_ACTIVE_SCHEDULE_ACTIVITY))
            currentCaller = this.getResources().getString(R.string.WS_ALL_ACTIVE_SCHEDULES_BY_ISP);
        else
            currentCaller = this.getResources().getString(R.string.WS_ALL_SCHEDULES_BY_ISP);
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
    }

    private void loadFooter() {
        int layoutId = 0;
        if(Pages.ISP_ACTIVE_SCHEDULE_ACTIVITY == currentActivity)
            layoutId = R.layout.content_isp_schedule_header;
        else
            layoutId = R.layout.content_isp_history_schedule_header;
        View footerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null, false);
    }

    private void loadHeader() {
        int layoutId = 0;
        if(Pages.ISP_ACTIVE_SCHEDULE_ACTIVITY == currentActivity)
            layoutId = R.layout.content_isp_schedule_header;
        else
            layoutId = R.layout.content_isp_history_schedule_header;

        View headerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null, false);
        grid.addHeaderView(headerView);
    }

    //@Override
    public void onClick(View view, String callerIdentifier) {
        if(this.getResources().getString(R.string.p_header).equals(callerIdentifier)){
            TextView textView = (TextView)view;
            Toast.makeText(this,textView.getText().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResult(JSONObject jsonObject) {
        Log.i(TAG,jsonObject.toString());
        if(currentCaller == null)return;
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_ALL_ACTIVE_SCHEDULES_BY_ISP))  || currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_ALL_SCHEDULES_BY_ISP))){
            try {
                bookings = ReflectionUtil.mapJson2Bean(jsonObject.getJSONArray(getString(R.string.param_results)),Booking.class);
                int[] items = null;
                int rowLayoutId = 0;
                if(Pages.ISP_ACTIVE_SCHEDULE_ACTIVITY == currentActivity) {
                    items[0] = R.id.isp_schedule_sno;
                    items[1] = R.id.isp_schedule_username;
                    items[2] = R.id.isp_schedule_phonenumber;
                    items[3] = R.id.isp_schedule_action;
                    rowLayoutId = R.layout.content_isp_schedule_row;
                }else {
                    items[0] = R.id.isp_history_schedule_sno;
                    items[1] = R.id.isp_schedule_username;
                    items[2] = R.id.isp_history_schedule_phonenumber;
                    items[3] = R.id.isp_history_schedule_date;
                    rowLayoutId = R.layout.content_isp_history_schedule_row;
                }

                CommonArrayAdapter commonArrayAdapter = new CommonArrayAdapter(this,bookings,rowLayoutId,items);
                grid.setAdapter(commonArrayAdapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        booking = bookings.get(position-1);
                        YourTimeUtil.dialog(IspScheduleActivity.this,getString(R.string.your_time_says),getString(R.string.question_on_click_grid_reschedule_cancel),android.R.drawable.ic_input_get);
                        Toast.makeText(IspScheduleActivity.this,"Clicked on position "+booking.getUsername(),Toast.LENGTH_SHORT).show();
                    }
                });

                loadHeader();
                //loadFooter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(currentCaller.equals(this.getResources().getString(R.string.WS_APPOINTMENT_CANCEL_BY_CONSUMER))){
            try {
                if(jsonObject.getBoolean(getString(R.string.param_status))){
                    YourTimeUtil.dialog(this,getString(R.string.your_time_says),getString(R.string.schedule_cancel_success),android.R.drawable.ic_dialog_info);
                    loadSchedules();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuItemId = 0;
        switch (currentActivity){
            case ISP_ACTIVE_SCHEDULE_ACTIVITY:
                menuItemId = R.id.isp_action_schedule;
                break;
            case ISP_SCHEDULE_HISTORY_ACTIVITY:
                menuItemId = R.id.isp_action_history;
                break;
            default:
                Log.w(TAG,"Consider to add "+currentActivity.name());
        }
        YourTimeUtil.controlMenuShowHide(this,menu,menuItemId,currentActivity);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        YourTimeUtil.triggerMenuItemSelection(this,item.getItemId(),currentActivity,getSessionManager());
        return super.onOptionsItemSelected(item);
    }

    public  void reschedule(View view){
        Toast.makeText(this,"Reschedule will be invoked.",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra(this.getResources().getString(R.string.caller), currentActivity);
        intent.putExtra(this.getResources().getString(R.string.actAs), Pages.ISP_SCHEDULE_UPDATE_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public  void cancelSchedule(View view){
        Map<String, Object> params = new HashMap<String,Object>();
        Booking booking = new Booking();
        booking.setUsername(getSessionManager().getUserDetails().getUsername());
        params.put(this.getResources().getString(R.string.ws_param),booking);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.WS_SCHEDULE_CANCEL_BY_ISP) ;
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
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
