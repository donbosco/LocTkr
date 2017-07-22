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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.your.time.bean.Status;
import com.your.time.bean.User;
import com.your.time.custom.adapter.CommonArrayAdapter;
import com.your.time.util.Pages;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsumerHomeActivity extends YourTimeActivity implements RestCaller{

    ListView grid;

    private static final String TAG = "ConsumerHomeActivity";
    private static String currentCaller = null;
    private List<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentActivity = Pages.CONSUMER_HOME_ACTIVITY;
        activity = this;
        super.onCreate(savedInstanceState);
    }

    public void loadUI(){
        setContentView(R.layout.activity_consumer_home);
        grid =(ListView) findViewById(R.id.consumer_appointment_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.consumer_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.consumer_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final List<User> users = new ArrayList<User>();
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
            int[] items = {R.id.consumer_home_sno,R.id.consumer_home_username,R.id.consumer_home_phonenumber,R.id.consumer_home_action};
            CommonArrayAdapter commonArrayAdapter = new CommonArrayAdapter(this,users,R.layout.content_consumer_home_row,items);
            grid.setAdapter(commonArrayAdapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    User user = users.get(position-1);
                    Toast.makeText(ConsumerHomeActivity.this,"Clicked on position "+user.getUsername(),Toast.LENGTH_SHORT).show();
                }
            });

            loadHeader();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*Map<String, Object> params = new HashMap<String,Object>();
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setUsername("9500429891");
        params.put(this.getResources().getString(R.string.ws_param),serviceProvider);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.ws_consumer_appointments_fetch) ;
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();*/
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
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.ws_consumer_appointments_fetch))){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                users = objectMapper.readValue(jsonObject.getJSONArray("results").toString(),TypeFactory.defaultInstance().constructCollectionType(List.class,User.class));
                int[] items = {R.id.consumer_home_sno,R.id.consumer_home_username,R.id.consumer_home_phonenumber,R.id.consumer_home_action};
                CommonArrayAdapter commonArrayAdapter = new CommonArrayAdapter(this,users,R.layout.content_consumer_home_row,items);
                grid.setAdapter(commonArrayAdapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = users.get(position-1);
                        Toast.makeText(ConsumerHomeActivity.this,"Clicked on position "+user.getUsername(),Toast.LENGTH_SHORT).show();
                    }
                });

                loadHeader();
                //loadFooter();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_consumer_home, menu);
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
            case R.id.consumer_action_book:
                intent = new Intent(this, BookActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.CONSUMER_HOME_ACTIVITY);
                startActivity(intent);
                finish();
                break;
            case R.id.consumer_action_settings:
                intent = new Intent(this, BookActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.CONSUMER_HOME_ACTIVITY);
                startActivity(intent);
                finish();
                break;
            case R.id.consumer_action_logout:
                SESSION_MANAGER.logoutUser(this);
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.CONSUMER_HOME_ACTIVITY);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
