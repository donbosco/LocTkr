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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.your.time.bean.HeaderInfo;
import com.your.time.util.Pages;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookActivity extends YourTimeActivity implements RestCaller{

    private static final String TAG = "BookActivity";
    private static String currentCaller = null;
    List<HeaderInfo> myServiceTypes = new ArrayList<HeaderInfo>();
    private String selectedServiceType = null;
    private ProgressDialog progressDialog = null;

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
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void book() {
        Log.d(TAG, "Book");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _bookButton.setEnabled(false);
        progressDialog = new ProgressDialog(BookActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String bookingDate = _bookingDate.getText().toString();
        String bookingTime = _bookingTime.getText().toString();
    }


    public void onSignupSuccess() {
        _bookButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

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
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.ws_service_type_fetch))){

        }else if (currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.ws_sign_up))){
            progressDialog.dismiss();
            Toast.makeText(this,"Created user",Toast.LENGTH_SHORT);
            Intent intent = new Intent(this, BookActivity.class);
            startActivity(intent);
            finish();
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
}
