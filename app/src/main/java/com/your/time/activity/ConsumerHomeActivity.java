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

public class ConsumerHomeActivity extends YourTimeActivity implements RestCaller{

    ListView grid;

    private static final String TAG = "ConsumerHomeActivity";
    private static String currentCaller = null;
    private List<Booking> bookings = new ArrayList<Booking>();
    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentActivity = Pages.CONSUMER_HOME_ACTIVITY;
        activity = this;
        super.onCreate(savedInstanceState);
    }

    public void loadUI(){
        setContentView(R.layout.activity_consumer_home);
        grid =(ListView) findViewById(R.id.consumer_home_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_consumer_home);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.consumer_home_fab);
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
        //if(currentActivity.equals(Pages.CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY))
            currentCaller = this.getResources().getString(R.string.WS_ALL_ACTIVE_APPOINTMENTS_BY_CONSUMER);
       /* else
            currentCaller = this.getResources().getString(R.string.WS_ALL_APPOINTMENTS_BY_CONSUMER);*/

        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
    }

    private void loadFooter() {
        View footerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.content_consumer_home_header, null, false);
    }

    private void loadHeader() {
        View headerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.content_consumer_home_header, null, false);
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
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_ALL_ACTIVE_APPOINTMENTS_BY_CONSUMER))){
            try {
                bookings = ReflectionUtil.mapJson2Bean(jsonObject.getJSONArray(getString(R.string.param_results)),Booking.class);
                int[] items = new int[3];
                int rowLayoutId = 0;
                    items[0] = R.id.consumer_home_status;
                    items[1] = R.id.consumer_home_service;
                    items[2] = R.id.consumer_home_waitTime;
                    rowLayoutId = R.layout.content_consumer_home_row;

                CommonArrayAdapter commonArrayAdapter = new CommonArrayAdapter(this,bookings,rowLayoutId,Pages.CONSUMER_ACTIVE_APPOINTMENT_ACTIVITY);
                grid.setAdapter(commonArrayAdapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        booking = bookings.get(position-1);
                        //dialog = YourTimeUtil.dialog(ConsumerHomeActivity.this,getString(R.string.your_time_says),getString(R.string.question_on_click_grid_reschedule_cancel),R.drawable.question);
                        Toast.makeText(ConsumerHomeActivity.this,"Clicked on position "+booking.getUsername(),Toast.LENGTH_SHORT).show();
                    }
                });

                if(grid.getHeaderViewsCount() == 0)
                    loadHeader();
                //loadFooter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(currentCaller.equals(this.getResources().getString(R.string.WS_APPOINTMENT_CANCEL_BY_CONSUMER))){
            try {
                if(jsonObject.getBoolean(getString(R.string.param_status))){
                    dialog = YourTimeUtil.dialog(this,getString(R.string.your_time_says),getString(R.string.consumer_appointment_cancel_success),R.drawable.info);
                    loadAppointments();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        YourTimeUtil.controlMenuShowHide(this,menu,R.id.consumer_action_home,currentActivity);
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

    public  void reschedule(View view){
        booking = bookings.get((Integer)view.getTag(R.string.param_booking));
        Toast.makeText(ConsumerHomeActivity.this,"Reschedule will be invoked.",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ConsumerHomeActivity.this, BookActivity.class);
        intent.putExtra(ConsumerHomeActivity.this.getResources().getString(R.string.caller), currentActivity);
        intent.putExtra(ConsumerHomeActivity.this.getResources().getString(R.string.actAs), Pages.CONSUMER_APPOINTMENT_UPDATE_ACTIVITY);
        intent.putExtra(getString(R.string.param_booking),ReflectionUtil.mapBean2Json(booking));
        dialogClose();
        startActivity(intent);
        finish();
    }

    public  void cancelSchedule(View view){
        Map<String, Object> params = new HashMap<String,Object>();
        /*Booking booking = new Booking();
        booking.setUsername(getSessionManager().getUserDetails().getUsername());*/
        booking = bookings.get((Integer)view.getTag(R.string.param_booking));
        params.put(this.getResources().getString(R.string.ws_param),this.booking);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.WS_APPOINTMENT_CANCEL_BY_CONSUMER) ;
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
        dialogClose();
    }

}