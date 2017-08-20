package com.your.time.activity;

import android.app.Dialog;
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

public class ConsumerAppointmentActivity extends YourTimeActivity implements RestCaller{

    ListView grid;
    Dialog dialog;

    private static final String TAG = "ConsumerAppointment";
    private static String currentCaller = null;
    private List<Booking> bookings = new ArrayList<Booking>();
    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
    }

    public void loadUI(){
        setContentView(R.layout.activity_consumer_appointment);
        grid =(ListView) findViewById(R.id.consumer_appointment_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.consumer_appointment_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.consumer_appointment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*bookings.add(new Booking(null,null,null, null, "Clinic","1234567890", "25/07/2017", "11:20:21", null));
        bookings.add(new Booking(null,null,null, null, "Saloon","1234567890", "23/07/2017", "11:20:21", null));
        bookings.add(new Booking(null,null,null, null, "Textile","1234567890", "24/07/2017", "11:20:21", null));

        Status<Booking> status = new Status<Booking>();
        status.setResults(bookings);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String string = objectMapper.writeValueAsString(status.getResults());
            status = null;
            List<Booking> l = objectMapper.readValue(string, TypeFactory.defaultInstance().constructCollectionType(List.class, Booking.class));
            int[] items = {R.id.consumer_home_sno,R.id.consumer_home_service,R.id.consumer_home_phonenumber,R.id.consumer_home_waitTime};
            CommonArrayAdapter commonArrayAdapter = new CommonArrayAdapter(this,bookings,R.layout.content_consumer_home_row,items);
            grid.setAdapter(commonArrayAdapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Booking booking = bookings.get(position-1);
                    Toast.makeText(ConsumerHomeActivity.this,"Clicked on position "+booking.getUsername(),Toast.LENGTH_SHORT).show();
                }
            });

            loadHeader();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        loadAppointments();
    }

    private void loadAppointments() {
        Map<String, Object> params = new HashMap<String,Object>();
        Booking booking = new Booking();
        booking.setUsername(getSessionManager().getUserDetails().getUsername());
        params.put(this.getResources().getString(R.string.ws_param),booking);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        if(currentActivity.equals(Pages.CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY))
            currentCaller = this.getResources().getString(R.string.WS_ALL_ACTIVE_APPOINTMENTS_BY_CONSUMER);
        else
            currentCaller = this.getResources().getString(R.string.WS_ALL_APPOINTMENTS_BY_CONSUMER);

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

    private void loadFooter() {
        int layoutId = 0;
        if(Pages.CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY == currentActivity)
            layoutId = R.layout.content_consumer_appointment_header;
        else
            layoutId = R.layout.content_consumer_history_appointment_header;
        View footerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null, false);
    }

    private void loadHeader() {
        int layoutId = 0;
        if(Pages.CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY == currentActivity)
            layoutId = R.layout.content_consumer_appointment_header;
        else
            layoutId = R.layout.content_consumer_history_appointment_header;

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
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_ALL_ACTIVE_APPOINTMENTS_BY_CONSUMER))  || currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_ALL_APPOINTMENTS_BY_CONSUMER))){
            try {
                bookings = ReflectionUtil.mapJson2Bean(jsonObject.getJSONArray(getString(R.string.param_results)),Booking.class);
                int[] items = null;
                int rowLayoutId = 0;
                if(Pages.CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY == currentActivity) {
                    items[0] = R.id.consumer_appointment_sno;
                    items[1] = R.id.consumer_appointment_service;
                    items[2] = R.id.consumer_appointment_phonenumber;
                    items[3] = R.id.consumer_appointment_waitTime;
                    rowLayoutId = R.layout.content_consumer_appointment_row;
                }else {
                    items[0] = R.id.consumer_history_appointment_sno;
                    items[1] = R.id.consumer_history_appointment_service;
                    items[2] = R.id.consumer_history_appointment_phonenumber;
                    items[3] = R.id.consumer_history_appointment_date;
                    rowLayoutId = R.layout.content_consumer_hostory_appointment_row;
                }

                CommonArrayAdapter commonArrayAdapter = new CommonArrayAdapter(this,bookings,rowLayoutId,items);
                grid.setAdapter(commonArrayAdapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        booking = bookings.get(position-1);
                        dialog = YourTimeUtil.dialog(ConsumerAppointmentActivity.this,getString(R.string.your_time_says),getString(R.string.question_on_click_grid_reschedule_cancel),R.drawable.ic_question);
                        Toast.makeText(ConsumerAppointmentActivity.this,"Clicked on position "+booking.getUsername(),Toast.LENGTH_SHORT).show();
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
                    dialog = YourTimeUtil.dialog(this,getString(R.string.your_time_says),getString(R.string.appointment_cancel_success),R.drawable.ic_info);
                    loadAppointments();
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
            case CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY:
                menuItemId = R.id.consumer_action_appointment;
                break;
            case CONSUMER_APPOINTMENT_HISTORY_ACTIVITY:
                menuItemId = R.id.consumer_action_history;
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
        Toast.makeText(ConsumerAppointmentActivity.this,"Reschedule will be invoked.",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ConsumerAppointmentActivity.this, BookActivity.class);
        intent.putExtra(ConsumerAppointmentActivity.this.getResources().getString(R.string.caller), currentActivity);
        intent.putExtra(ConsumerAppointmentActivity.this.getResources().getString(R.string.actAs), Pages.CONSUMER_APPOINTMENT_UPDATE_ACTIVITY);
        dialogClose();
        startActivity(intent);
        finish();
    }

    public  void cancelSchedule(View view){
        Map<String, Object> params = new HashMap<String,Object>();
        /*Booking booking = new Booking();
        booking.setUsername(getSessionManager().getUserDetails().getUsername());*/
        params.put(this.getResources().getString(R.string.ws_param),this.booking);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.WS_APPOINTMENT_CANCEL_BY_CONSUMER) ;
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
        dialogClose();
    }
}