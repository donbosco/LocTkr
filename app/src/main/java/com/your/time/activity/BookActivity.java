package com.your.time.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.your.time.bean.Booking;
import com.your.time.util.Pages;
import com.your.time.util.RestServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookActivity extends YourTimeActivity implements RestCaller{

    private static final String TAG = "BookActivity";
    private static String currentCaller = null;
    private String serviceProviderId;

    @Bind(R.id.input_booking_date)
    EditText _bookingDate;
    @Bind(R.id.input_booking_time)
    EditText _bookingTime;

    @Bind(R.id.btn_book)
    Button _bookButton;
    @Bind(R.id.btn_cancel)
    Button _cancelButton;

    DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentActivity = Pages.BOOK_ACTIVITY;
        activity = this;
        super.onCreate(savedInstanceState);
        if(callingFrom == Pages.SIGN_UP_ACTIVITY) {
            serviceProviderId = this.getIntent().getExtras().getString(getString(R.string.param_service_provider_id));
        }
    }
    public void loadUI(){
        setContentView(R.layout.activity_book);
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

        _bookingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    pickDate();
                }
            }
        });

        _bookingTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    pickTime();
                }
            }
        });

        _bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book();
            }
        });

        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConsumerHomeActivity.class);
                intent.putExtra(BookActivity.this.getResources().getString(R.string.caller), Pages.BOOK_ACTIVITY);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void book() {
        Log.d(TAG, "Book");
        if (!validate()) {
            onValidationFailed();
            return;
        }
        _bookButton.setEnabled(false);
        progressDialog = new ProgressDialog(BookActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.booking_processing_message));
        progressDialog.show();

        String bookingDate = _bookingDate.getText().toString();
        String bookingTime = _bookingTime.getText().toString();

        Booking booking = new Booking();
        booking.setServiceProviderId(serviceProviderId);
        booking.setDate(bookingDate);
        booking.setTime(bookingTime);
        booking.setUsername(SESSION_MANAGER.getUsername());
        booking.setUserDetail(SESSION_MANAGER.getUserDetails());

        Map<String, Object> params = new HashMap<String,Object>();
        params.put(this.getResources().getString(R.string.ws_param),booking);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.ws_consumer_booking);
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();

    }

    public void onValidationFailed() {
        Toast.makeText(getBaseContext(), "Check your input", Toast.LENGTH_LONG).show();
        _bookButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        return valid;
    }

    @Override
    public void onWebServiceResult(JSONObject jsonObject) {
        Log.i(TAG,jsonObject.toString());
        if(currentCaller == null)return;
        else if (currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.ws_consumer_booking))){
            try {
                progressDialog.dismiss();
                boolean isBooked = jsonObject.getBoolean(getString(R.string.param_status));
                if(isBooked) {

                    Toast.makeText(this, R.string.your_booking_confirmed_message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ConsumerHomeActivity.class);
                    intent.putExtra(this.getResources().getString(R.string.caller), Pages.BOOK_ACTIVITY);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, jsonObject.getString(getString(R.string.param_message)), Toast.LENGTH_SHORT).show();
                    _bookButton.setEnabled(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        currentCaller = null;
    }

    public void pickDate(){
        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                _bookingDate.setText(fmtDateAndTime.format(myCalendar.getTime()));
            }
        };
    }

    public void pickTime(){
        TimePickerDialog.OnTimeSetListener  d = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int min) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                myCalendar.set(Calendar.MINUTE, min);
                _bookingTime.setText(fmtDateAndTime.format(myCalendar.getTime()));
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_consumer_book, menu);
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
            case R.id.consumer_action_book_home:
                intent = new Intent(this, ConsumerHomeActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.BOOK_ACTIVITY);
                startActivity(intent);
                finish();
                break;
            case R.id.consumer_action_book_settings:
                intent = new Intent(this, IspSettingActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.BOOK_ACTIVITY);
                startActivity(intent);
                finish();
                break;
            case R.id.consumer_action_book_logout:
                callingFrom = Pages.BOOK_ACTIVITY;
                super.logout(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
