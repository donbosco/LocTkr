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
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();
        Map<String, Object> params = new HashMap<String,Object>();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        params.put(this.getResources().getString(R.string.ws_param),user);
        params.put(this.getResources().getString(R.string.ws_method),this.getResources().getString(R.string.post));
        currentCaller = this.getResources().getString(R.string.ws_authendicate) ;
        params.put(this.getResources().getString(R.string.ws_url),currentCaller);
        new RestServiceHandler(this, params,this).execute();
    }

    public void onLoginSuccess(User user) {
        if(user != null) {
            _loginButton.setEnabled(false);
            SESSION_MANAGER.createLoginSession(this, user.getFirstname(), user.getEmail(), user.getUsername(), user.getPhonenumber(), user.isServiceProvider(), user.getServiceProviderId(), user.getRole());
            Intent intent = null;
            if(user.isServiceProvider()) {
                intent = new Intent(this, IspHomeActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.LOGIN_ACTIVITY);
            }else{
                intent = new Intent(this, ConsumerHomeActivity.class);
                intent.putExtra(this.getResources().getString(R.string.caller), Pages.LOGIN_ACTIVITY);
            }
            progressDialog.dismiss();
            startActivity(intent);
            finish();
        }else{
            onLoginFailed();
            Toast.makeText(this,"Unable to get the details, Please try login again.",Toast.LENGTH_SHORT).show();
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
            _usernameText.setError("Enter a valid username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            _passwordText.setError("Password must be between 3 and 10 alphanumeric characters");
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
        else if(currentCaller.equalsIgnoreCase(this.getResources().getString(R.string.ws_authendicate))) {
            try {
                boolean status = jsonObject.getBoolean("status");
                String message = jsonObject.getString("message");
                JSONObject userJson = jsonObject.getJSONObject("result");
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

    /*private class LongRunningGetIO extends AsyncTask<Void, Void, String> {

        String username;
        String password;

        public LongRunningGetIO(String username, String password) {
            this.username = username;
            this.password = password;
        }

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("http://192.168.42.232:8080/YourTime/users/authendicate/" + username + "/" + password);
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                Log.e("LoginActivity", e.getLocalizedMessage(), e);
                return e.getLocalizedMessage();
            }
            return text;
        }


        protected void onPostExecute(String results) {
            try {
                JSONObject jObject = new JSONObject(results);
                boolean status = jObject.getBoolean("status");
                String message = jObject.getString("message");
                if (status) {
                    onLoginSuccess();
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
    }*/
}