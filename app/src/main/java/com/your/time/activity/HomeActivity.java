package com.your.time.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.your.time.bean.ServiceProvider;
import com.your.time.bean.User;
import com.your.time.custom.adapter.CommonArrayAdapter;
import com.your.time.util.RestServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements RestCaller{

    ListView grid;

    private static final String TAG = "HomeActivity";
    private static String currentCaller = null;
    private ProgressDialog progressDialog = null;
    private List<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        grid =(ListView) findViewById(R.id.scheduleGrid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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


        Map<String, Object> params = new HashMap<String,Object>();
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setUsername("9500429891");
        params.put(this.getResources().getString(R.string.ws_param),serviceProvider);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.ws_isp_schedules_fetch) ;
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
    }

    private void loadFooter() {
        View footerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.content_home_header, null, false);
    }

    private void loadHeader() {
        View headerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.content_home_header, null, false);
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
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.ws_isp_schedules_fetch))){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                users = objectMapper.readValue(jsonObject.getJSONArray("results").toString(),TypeFactory.defaultInstance().constructCollectionType(List.class,User.class));

                int[] items = {R.id.home_sno,R.id.home_username,R.id.home_phonenumber,R.id.home_action};

                CommonArrayAdapter commonArrayAdapter = new CommonArrayAdapter(this,users,R.layout.content_home_row,items);
                grid.setAdapter(commonArrayAdapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = users.get(position-1);
                        Toast.makeText(HomeActivity.this,"Clicked on position "+user.getUsername(),Toast.LENGTH_SHORT).show();
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
}
