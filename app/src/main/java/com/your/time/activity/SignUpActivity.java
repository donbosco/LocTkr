package com.your.time.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.your.time.adapter.MyExpandableListAdapter;
import com.your.time.bean.DetailInfo;
import com.your.time.bean.HeaderInfo;
import com.your.time.bean.Rest;
import com.your.time.bean.User;
import com.your.time.util.RestServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements RestCaller{

    private static final String TAG = "SignupActivity";
    private ExpandableListAdapter expandableListAdapter;
    private static String currentCaller = null;
    List<HeaderInfo> myServiceTypes = new ArrayList<HeaderInfo>();
    private String selectedServiceType = null;

    @Bind(R.id.input_first_name)
    EditText _firstName;
    @Bind(R.id.input_last_name)
    EditText _lastName;
    @Bind(R.id.input_address1)
    EditText _addressLine1;
    @Bind(R.id.input_address2)
    EditText _addressLine2;
    @Bind(R.id.input_state)
    EditText _state;
    @Bind(R.id.input_country)
    EditText _country;
    @Bind(R.id.input_zip_code)
    EditText _zip;
    @Bind(R.id.input_email)
    EditText _email;
    @Bind(R.id.input_phone)
    EditText _phone;
    @Bind(R.id.input_password)
    EditText _userPassword;
    @Bind(R.id.input_confirm_password)
    EditText _userConfirmPassword;
    @Bind(R.id.lst_company_spec)
    ExpandableListView serviceType;

    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_signin)
    TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

        ButterKnife.bind(this);

        Map<String, User> params = new HashMap<String,User>();
        params.put(this.getResources().getString(R.string.ws_param),null);
        currentCaller = this.getResources().getString(R.string.ws_service_type_fetch) ;
        serviceType = (ExpandableListView) findViewById(R.id.lst_company_spec);
        new RestServiceHandler(this, params,this.getResources().getString(R.string.get)).execute();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
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

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstname = _firstName.getText().toString();
        String lastname = _lastName.getText().toString();
        String addressLine1 = _addressLine1.getText().toString();
        String addressLine2 = _addressLine1.getText().toString();
        String state = _state.getText().toString();
        String country = _country.getText().toString();
        String zipCode = _zip.getText().toString();
        String email = _email.getText().toString();
        String mobile = _phone.getText().toString();
        String userPassword = _userPassword.getText().toString();
        String userConfirmPassword = _userConfirmPassword.getText().toString();

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setAddressline1(addressLine1);
        user.setAddressline2(addressLine2);
        user.setState(state);
        user.setCountry(country);
        user.setZip(zipCode);
        user.setEmail(email);
        user.setPhonenumber(mobile);
        user.setPassword(userPassword);
        user.setConfirmPassword(userConfirmPassword);
        user.setServiceProvider(selectedServiceType==null?false:true);
        user.setRole("User");

        Map<String, User> params = new HashMap<String, User>();
        params.put(this.getResources().getString(R.string.ws_param),user);
        currentCaller = this.getResources().getString(R.string.ws_sign_up) ;
        new RestServiceHandler(this,params,this.getResources().getString(R.string.post)).execute();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        /*String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }*/

        return valid;
    }

    @Override
    public void onWebServiceResult(JSONObject jsonObject) {
        Log.i(TAG,jsonObject.toString());
        if(currentCaller == null)return;
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.ws_service_type_fetch))){
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("ServiceTypes");
                HeaderInfo headerInfo = new HeaderInfo();
                for (int i=0; i < jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    DetailInfo detailInfo = new DetailInfo();
                    detailInfo.setName(object.getString("value"));
                    detailInfo.setSequence(""+(i+1));
                    headerInfo.getList().add(detailInfo);
                }
                headerInfo.setName("Are you service provider?");
                myServiceTypes.add(headerInfo);

                //listener for child row click
                serviceType.setOnChildClickListener(myListItemClicked);
                //listener for group heading click
                serviceType.setOnGroupClickListener(myListGroupClicked);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            MyExpandableListAdapter listAdapter = new MyExpandableListAdapter(this, myServiceTypes);
            serviceType.setAdapter(listAdapter);
        }else if (currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.ws_sign_up))){
            Toast.makeText(this,"Created user",Toast.LENGTH_SHORT);
        }
        currentCaller = null;
    }

    private ExpandableListView.OnChildClickListener myListItemClicked =  new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {

            //get the group header
            HeaderInfo headerInfo = myServiceTypes.get(groupPosition);
            //get the child info
            DetailInfo detailInfo =  headerInfo.getList().get(childPosition);
            //display it or do something with it
            Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo.getName()
                    + "/" + detailInfo.getName(), Toast.LENGTH_LONG).show();
            return false;
        }
    };

    //our group listener
    private ExpandableListView.OnGroupClickListener myListGroupClicked =  new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {

            //get the group header
            HeaderInfo headerInfo = myServiceTypes.get(groupPosition);
            //display it or do something with it
            Toast.makeText(getBaseContext(), "Child on Header " + headerInfo.getName(),
                    Toast.LENGTH_LONG).show();

            return false;
        }
    };

}
