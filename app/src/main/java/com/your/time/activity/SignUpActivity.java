package com.your.time.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.your.time.bean.DetailInfo;
import com.your.time.bean.HeaderInfo;
import com.your.time.bean.MasterData;
import com.your.time.bean.User;
import com.your.time.custom.adapter.MyExpandableListAdapter;
import com.your.time.util.Pages;
import com.your.time.util.RestServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends YourTimeActivity implements RestCaller{

    private static final String TAG = "SignUpActivity";
    List<HeaderInfo> myServiceTypes = new ArrayList<HeaderInfo>();
    private String selectedServiceType = null;
    private static String currentCaller = null;
    private User user;

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
        currentActivity = Pages.SIGN_UP_ACTIVITY;
        activity = this;
        super.onCreate(savedInstanceState);
    }
    public void loadUI(){
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.signup_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);

        Map<String, Object> params = new HashMap<String,Object>();
        MasterData masterData = new MasterData();
        masterData.setType(this.getResources().getString(R.string.static_service_type));
        params.put(this.getResources().getString(R.string.ws_param),masterData);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.WS_FETCH_ANY_ACTIVE_TYPE) ;
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        serviceType = (ExpandableListView) findViewById(R.id.lst_company_spec);
        new RestServiceHandler(this, params,this).execute();

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
        progressDialog = new ProgressDialog(SignUpActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.user_acc_creating_message));
        progressDialog.show();

        updateModel();

        Map<String, Object> params = new HashMap<String,Object>();
        MasterData masterData = new MasterData();
        masterData.setType(this.getResources().getString(R.string.static_service_type));
        params.put(this.getResources().getString(R.string.ws_param),user);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.WS_SIGN_UP);
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_LONG).show();

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
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_FETCH_ANY_ACTIVE_TYPE))){
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.param_results));
                HeaderInfo headerInfo = new HeaderInfo();
                for (int i=0; i < jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    DetailInfo detailInfo = new DetailInfo();
                    detailInfo.setName(object.getString("value"));
                    detailInfo.setSequence(""+(i+1));
                    headerInfo.getList().add(detailInfo);
                }
                headerInfo.setName(getString(R.string.are_you_isp));
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

            // setOnChildClickListener listener for child row click
            serviceType.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    TextView textView = (TextView) (((LinearLayout) v).getChildAt(0));
                    selectedServiceType = textView.getText().toString();
                    //display it or do something with it
                    Toast.makeText(getBaseContext(), " Clicked on :: " +selectedServiceType, Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            // setOnGroupClickListener listener for group heading click
            serviceType.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    TextView textView = (TextView) (((LinearLayout) v).getChildAt(0));
                    //display it or do something with it
                    Toast.makeText(getBaseContext(), " Clicked on :: " +textView.getText(), Toast.LENGTH_LONG).show();
                    int hei=v.getLayoutParams().height;
                    System.out.println("Height   "+hei);
                    if (hei== LinearLayout.LayoutParams.WRAP_CONTENT) {
                        int height = hei;
                        if(!serviceType.isGroupExpanded(groupPosition)) {
                            HeaderInfo headerInfo = ((HeaderInfo) parent.getExpandableListAdapter().getGroup(0));
                            for (DetailInfo detailInfo : headerInfo.getList()) {
                                height += 60;
                            }
                        }
                        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,height));
                        parent.requestLayout();
                    }
                    else {
                        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        parent.requestLayout();
                    }
                    return  serviceType.isGroupExpanded(groupPosition) ? serviceType.collapseGroup(groupPosition) : serviceType.expandGroup(groupPosition);
                }
            });

        }else if (currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_SIGN_UP))){
            progressDialog.dismiss();
            try {
                boolean isSignedUp = jsonObject.getBoolean(getString(R.string.param_status));
                if (isSignedUp) {
                    if (callingFrom == Pages.MAPS_ACTIVITY) {
                        Toast.makeText(this, R.string.user_creation_success_message, Toast.LENGTH_SHORT);
                        String serviceProviderId = this.getIntent().getExtras().getString(this.getResources().getString(R.string.param_service_provider_id));
                        Intent intent = new Intent(this, BookActivity.class);
                        intent.putExtra(this.getResources().getString(R.string.caller), Pages.SIGN_UP_ACTIVITY);
                        intent.putExtra(this.getResources().getString(R.string.actAs), Pages.CONSUMER_APPOINTMENT_ADD_ACTIVITY);
                        intent.putExtra(this.getResources().getString(R.string.param_service_provider_id), serviceProviderId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, R.string.user_creation_success_message, Toast.LENGTH_SHORT);
                        Intent intent;
                        if(getSessionManager().getUserDetails().isServiceProvider()){
                            intent = new Intent(this, IspHomeActivity.class);
                            intent.putExtra(this.getResources().getString(R.string.actAs), Pages.ISP_HOME_ACTIVITY);
                        }else{
                            intent = new Intent(this,ConsumerHomeActivity.class);
                            intent.putExtra(this.getResources().getString(R.string.actAs), Pages.CONSUMER_HOME_ACTIVITY);
                        }
                        intent.putExtra(this.getResources().getString(R.string.caller), Pages.SIGN_UP_ACTIVITY);
                        startActivity(intent);
                        finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    @Override
    public boolean updateView() {
        return false;
    }

    @Override
    public boolean updateModel() {
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
        user.setServiceProvider(selectedServiceType != null);
        user.setServiceProviderTye(selectedServiceType);
        user.setRole(getString(R.string.default_role_user));
        return true;
    }
}
