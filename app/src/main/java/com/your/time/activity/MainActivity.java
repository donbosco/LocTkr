package com.your.time.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.your.time.util.Pages;


public class MainActivity extends YourTimeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentActivity = Pages.MAIN_ACTIVITY;
        activity = this;
        super.onCreate(savedInstanceState);
    }
    public void loadUI(){
        setContentView(R.layout.activity_main);
        /*final List<User> users = new ArrayList<User>();
        users.add(new User(null, "Don", null, null, null, null,null, null, null, null, null, null,"846546874", false, null));
        users.add(new User(null, "Bosco", null, null, null, null,null, null, null, null, null, null,"846546874", false, null));
        users.add(new User(null, "Rayappan", null, null, null, null,null, null, null, null, null, null,"846546874", false, null));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Status<User> status = new Status<User>();
            status.setResults(users);
            String string = objectMapper.writeValueAsString(status);
            JSONObject object = new JSONObject(string);
            JSONArray usersJson = object.getJSONArray("results");
            List<User> users1 = ReflectionUtil.mapJson2Bean(usersJson,User.class);
            Log.i("MainActivity",users1.get(0).getUsername());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    public void signin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(this.getResources().getString(R.string.caller), Pages.MAIN_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public void bookSchedule(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(this.getResources().getString(R.string.caller), Pages.MAIN_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public void signup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(this.getResources().getString(R.string.caller), Pages.MAIN_ACTIVITY);
        startActivity(intent);
        finish();
    }
}
