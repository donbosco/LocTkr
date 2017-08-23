package com.your.time.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.your.time.util.BookingStatus;
import com.your.time.util.Pages;
import com.your.time.util.ReflectionUtil;
import com.your.time.util.RestServiceHandler;
import com.your.time.util.YourTimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookActivity extends YourTimeActivity implements RestCaller{

    private static final String TAG = "BookActivity";
    private static String currentCaller = null;
    private boolean isIspSpecific;
    private Booking booking;

    @Bind(R.id.input_on_behalf_of)
    EditText _onBehalfOf;
    @Bind(R.id.input_booking_date)
    EditText _bookingDate;
    @Bind(R.id.input_booking_time)
    EditText _bookingTime;
    @Bind(R.id.input_booking_reason)
    EditText _bookingReason;


    @Bind(R.id.btn_book)
    Button _bookButton;
    @Bind(R.id.btn_cancel)
    Button _cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
    }
    public void loadUI(){
        setContentView(R.layout.activity_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.book_toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.book_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        booking = new Booking();
        if(callingFrom == Pages.SIGN_UP_ACTIVITY || callingFrom == Pages.MAPS_ACTIVITY) {
            booking.setServiceProviderId(this.getIntent().getExtras().getString(getString(R.string.param_service_provider_id)));
        }else if(Pages.isIspSpecific(callingFrom,true)){
            booking.setServiceProviderId(getSessionManager().getUserDetails().getServiceProviderId());
            findViewById(R.id.onBehalfOf).setVisibility(View.VISIBLE);
            isIspSpecific = true;
        }

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
                intent.putExtra(BookActivity.this.getResources().getString(R.string.caller), currentActivity);
                intent.putExtra(BookActivity.this.getResources().getString(R.string.actAs), callingFrom);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        currentCaller = getString(R.string.WS_VIEW_APPOINTMENT_DETAILS);

        String bookingId = (String) this.getIntent().getExtras().get(this.getResources().getString(R.string.param_booking_id));
        if(bookingId != null) {
            Booking booking = new Booking();
            booking.set_id(bookingId);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(this.getResources().getString(R.string.ws_param), booking);
            params.put(this.getResources().getString(R.string.ws_method), this.getResources().getString(R.string.post));
            params.put(this.getResources().getString(R.string.ws_url), currentCaller);
            new RestServiceHandler(this, params, this).execute();
        }
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
        switch (currentActivity){
            case CONSUMER_APPOINTMENT_ADD_ACTIVITY:
                progressDialog.setMessage(getString(R.string.booking_processing_message));
                currentCaller = this.getResources().getString(R.string.WS_BOOK_APPOINTMENT);
                booking.setStatus(BookingStatus.NEW.name());
                break;
            case CONSUMER_APPOINTMENT_UPDATE_ACTIVITY:
                progressDialog.setMessage(getString(R.string.booking_update_processing_message));
                currentCaller = this.getResources().getString(R.string.WS_APPOINTMENT_RESCHEDULE_BY_CONSUMER);
                booking.setStatus(BookingStatus.RESCHEDULED.name());
                break;
            case ISP_SCHEDULE_ADD_ACTIVITY:
                progressDialog.setMessage(getString(R.string.schedule_processing_message));
                currentCaller = this.getResources().getString(R.string.WS_CREATE_SCHEDULE_BY_ISP);
                booking.setStatus(BookingStatus.CONFIRMED.name());
                break;
            case ISP_SCHEDULE_UPDATE_ACTIVITY:
                progressDialog.setMessage(getString(R.string.schedule_update_processing_message));
                currentCaller = this.getResources().getString(R.string.WS_SCHEDULE_RESCHEDULE_BY_ISP);
                booking.setStatus(BookingStatus.RESCHEDULED.name());
                break;
        }

        progressDialog.show();

        String bookingDate = _bookingDate.getText().toString();
        String bookingTime = _bookingTime.getText().toString();
        String bookingReason = _bookingReason.getText().toString();

        booking.setDate(bookingDate);
        booking.setTime(bookingTime);
        booking.setReason(bookingReason);
        if(isIspSpecific){
            booking.setUsername(((EditText)findViewById(R.id.input_on_behalf_of)).getText().toString());
            currentCaller = this.getResources().getString(R.string.WS_CREATE_SCHEDULE_BY_ISP);
        }else {
            booking.setUsername(getSessionManager().getUsername());
            booking.setUserDetail(getSessionManager().getUserDetails());
            currentCaller = this.getResources().getString(R.string.WS_BOOK_APPOINTMENT);
        }

        Map<String, Object> params = new HashMap<String,Object>();
        params.put(this.getResources().getString(R.string.ws_param),booking);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
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
        else if (currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_BOOK_APPOINTMENT)) || currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_APPOINTMENT_RESCHEDULE_BY_CONSUMER))){
            try {
                progressDialog.dismiss();
                boolean isBooked = jsonObject.getBoolean(getString(R.string.param_status));
                if(isBooked) {

                    Toast.makeText(this, R.string.your_booking_confirmed_message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ConsumerHomeActivity.class);
                    intent.putExtra(this.getResources().getString(R.string.caller), Pages.BOOK_ACTIVITY);
                    intent.putExtra(this.getResources().getString(R.string.actAs), callingFrom);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, jsonObject.getString(getString(R.string.param_message)), Toast.LENGTH_SHORT).show();
                    _bookButton.setEnabled(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_CREATE_SCHEDULE_BY_ISP)) || currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_SCHEDULE_RESCHEDULE_BY_ISP))){
            try {
                progressDialog.dismiss();
                boolean isBooked = jsonObject.getBoolean(getString(R.string.param_status));
                if(isBooked) {

                    Toast.makeText(this, R.string.your_schedule_confirmed_message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, IspHomeActivity.class);
                    intent.putExtra(this.getResources().getString(R.string.caller), Pages.BOOK_ACTIVITY);
                    intent.putExtra(this.getResources().getString(R.string.actAs),callingFrom);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, jsonObject.getString(getString(R.string.param_message)), Toast.LENGTH_SHORT).show();
                    _bookButton.setEnabled(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_VIEW_APPOINTMENT_DETAILS))){
            try {
                progressDialog.dismiss();
                if(jsonObject.getBoolean(getString(R.string.param_status)))
                    booking = (Booking) ReflectionUtil.mapJson2Bean(jsonObject.getJSONObject(getString(R.string.param_result)),Booking.class);
                updateView();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        currentCaller = null;
    }

    public void pickDate(){
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(BookActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                _bookingDate.setText(format.format(myCalendar.getTime()));
            }
        },cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    public void pickTime(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(BookActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                myCalendar.set(Calendar.MINUTE, selectedMinute);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                _bookingTime.setText(format.format(myCalendar.getTime()));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuItemId = 0;
        switch (currentActivity){
            case ISP_SCHEDULE_ADD_ACTIVITY:
                menuItemId = R.id.isp_action_add_schedule;
                break;
            case ISP_SCHEDULE_UPDATE_ACTIVITY:
                menuItemId = R.id.isp_action_add_schedule;
                break;
            case CONSUMER_APPOINTMENT_ADD_ACTIVITY:
                menuItemId = R.id.isp_action_add_schedule;
                break;
            case CONSUMER_APPOINTMENT_UPDATE_ACTIVITY:
                menuItemId = R.id.consumer_action_book;
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

    @Override
    public boolean updateView() {
        _bookingDate.setText(booking.getDate());
        _bookingTime.setText(booking.getTime());
        _bookingReason.setText(booking.getReason());

        if(booking != null) {
            switch (currentActivity) {
                case ISP_SCHEDULE_ADD_ACTIVITY:
                    findViewById(R.id.onBehalfOf).setVisibility(View.VISIBLE);
                    _onBehalfOf.setVisibility(View.VISIBLE);
                    _onBehalfOf.setText(booking.getUsername());
                    break;
                case ISP_SCHEDULE_UPDATE_ACTIVITY:
                    findViewById(R.id.onBehalfOf).setVisibility(View.VISIBLE);
                    _onBehalfOf.setVisibility(View.VISIBLE);
                    _onBehalfOf.setText(booking.getUsername());
                    break;
                case CONSUMER_APPOINTMENT_ADD_ACTIVITY:
                case CONSUMER_APPOINTMENT_UPDATE_ACTIVITY:
                    findViewById(R.id.onBehalfOf).setVisibility(View.INVISIBLE);
                    _onBehalfOf.setVisibility(View.VISIBLE);
                    break;
                default:
                    Log.w(TAG, "Consider to add " + currentActivity.name());
            }
        }
        return true;
    }

    @Override
    public boolean updateModel() {

        if(this.booking == null)
            this.booking = new Booking();

        this.booking.setDate(_bookingDate.getText().toString());
        this.booking.setTime(_bookingTime.getText().toString());
        this.booking.setReason(_bookingReason.getText().toString());

        if(booking != null) {
            switch (currentActivity) {
                case ISP_SCHEDULE_ADD_ACTIVITY:
                case ISP_SCHEDULE_UPDATE_ACTIVITY:
                    this.booking.setUsername(_onBehalfOf.getText().toString());
                    break;
                case CONSUMER_APPOINTMENT_ADD_ACTIVITY:
                case CONSUMER_APPOINTMENT_UPDATE_ACTIVITY:
                    this.booking.setUsername(getSessionManager().getUsername());
                    break;
                default:
                    Log.w(TAG, "Consider to add " + currentActivity.name());
            }
        }
        return true;
    }
}
