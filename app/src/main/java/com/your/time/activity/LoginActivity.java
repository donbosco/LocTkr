package com.your.time.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.your.time.bean.User;
import com.your.time.util.Pages;
import com.your.time.util.ReflectionUtil;
import com.your.time.util.RestServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends YourTimeActivity implements RestCaller {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static String currentCaller = null;
    private static User user;

    @Bind(R.id.input_user)EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        currentActivity = Pages.LOGIN_ACTIVITY;
        activity = this;
        super.onCreate(savedInstanceState);
    }
    public void loadUI(){
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtra(LoginActivity.this.getResources().getString(R.string.caller), Pages.LOGIN_ACTIVITY);
                intent.putExtra(LoginActivity.this.getResources().getString(R.string.actAs), Pages.SIGN_UP_ACTIVITY);
                finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);
        progressDialog = new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authendication_progress_msg));
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();
        Map<String, Object> params = new HashMap<String,Object>();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        params.put(this.getResources().getString(R.string.ws_param),user);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.WS_AUTHENDICATE) ;
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
    }

    public void onLoginSuccess(User user) {
        if(user != null) {
            _loginButton.setEnabled(false);
            getSessionManager().createLoginSession(this, user.getFirstname(), user.getEmail(), user.getUsername(), user.getPhonenumber(), user.isServiceProvider(), user.getServiceProviderId(), user.getRole());
            Intent intent = null;
            if(user.isServiceProvider()) {
                intent = new Intent(this, IspHomeActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.LOGIN_ACTIVITY);
                intent.putExtra(this.getResources().getString(R.string.actAs), Pages.ISP_HOME_ACTIVITY);
            }else{
                intent = new Intent(this, ConsumerHomeActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.LOGIN_ACTIVITY);
                intent.putExtra(this.getResources().getString(R.string.actAs), Pages.CONSUMER_HOME_ACTIVITY);
            }
            progressDialog.dismiss();
            startActivity(intent);
            finish();
        }else{
            onLoginFailed();
            Toast.makeText(this, R.string.failed_fetching_details,Toast.LENGTH_SHORT).show();
        }
    }

    public void onLoginFailed() {
        progressDialog.dismiss();
        _usernameText.setText("");
        _passwordText.setText("");
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty() || username.length() == 0) {
            _usernameText.setError(getString(R.string.enter_valid_username));
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            _passwordText.setError(getString(R.string.password_constraints));
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }

    @Override
    public void onWebServiceResult(JSONObject jsonObject) {
        Log.i(TAG,jsonObject.toString());
        if(currentCaller == null)return;
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.WS_AUTHENDICATE))) {
            try {
                boolean status = jsonObject.getBoolean(getString(R.string.param_status));
                String message = jsonObject.getString(getString(R.string.param_message));
                JSONObject userJson = jsonObject.getJSONObject(getString(R.string.param_result));
                User user = (User) ReflectionUtil.mapJson2Bean(userJson,User.class);
                if (status) {
                    onLoginSuccess(user);
                } else {
                    onLoginFailed();
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public boolean updateView() {
        if(user != null) {
            _usernameText.setText(user.getUsername());
            _passwordText.setText(user.getPassword());
        }
        return true;
    }

    @Override
    public boolean updateModel() {
        if(user == null)
            user = new User();
        user.setUsername(_usernameText.getText().toString());
        user.setPassword(_passwordText.getText().toString());
        return true;
    }
}