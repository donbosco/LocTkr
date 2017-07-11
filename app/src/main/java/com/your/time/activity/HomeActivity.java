package com.your.time.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.your.time.bean.User;
import com.your.time.custom.adapter.CommonArrayAdapter;

public class HomeActivity extends AppCompatActivity{

    ListView grid;

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

        User[] users = new User[3];
        users[0] = new User(null, "Don", null, null, null, null,null, null, null, null, null, null,"8465468746164", false, null);
        users[1] = new User(null, "Bosco", null, null, null, null,null, null, null, null, null, null,"8465468746164", false, null);
        users[2] = new User(null, "Rayappan", null, null, null, null,null, null, null, null, null, null,"8465468746164", false, null);


        int[] items = {R.id.home_sno,R.id.home_username,R.id.home_phonenumber,R.id.home_action};

        CommonArrayAdapter commonArrayAdapter = new CommonArrayAdapter(this,users,R.layout.content_home_row,items);
        grid.setAdapter(commonArrayAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this,"Clicked on position "+position,Toast.LENGTH_SHORT).show();
            }
        });

        loadHeader();
        //loadFooter();
    }

    private void loadFooter() {
        View footerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.content_home_header, null, false);
    }

    private void loadHeader() {
        View headerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.content_home_header, null, false);
        /*GridView headerGrid = (GridView)footerView.findViewById(R.id.headerGrid);
        String[] data = new String[] {this.getResources().getString(R.string.sno),this.getResources().getString(R.string.name),this.getResources().getString(R.string.mobile),this.getResources().getString(R.string.action)};
        headerGrid.setAdapter(new StringArrayAdapter(this,data,this.getResources().getString(R.string.p_header),R.layout.content_home_header));*/
        //grid.addHeaderView(headerView);
    }

    //@Override
    public void onClick(View view, String callerIdentifier) {
        if(this.getResources().getString(R.string.p_header).equals(callerIdentifier)){
            TextView textView = (TextView)view;
            Toast.makeText(this,textView.getText().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
