package com.your.time.util;

import android.content.Context;
import android.os.AsyncTask;

import com.your.time.activity.R;
import com.your.time.activity.RestCaller;
import com.your.time.bean.Rest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class RestServiceHandler {

    private Context context;
    private RestCaller restCaller;
    private Map<String, Object> params;
    private final static String TAG = "RestServiceHandler";

    public RestServiceHandler(Context context, Map<String,Object> params,RestCaller caller) {
        this.context = context;
        this.params = params;
        this.restCaller = caller;
    }

    public void execute() {
        new RestServiceProcessor().execute();
    }

    private class RestServiceProcessor extends AsyncTask<Void, Void, String> {

        StringBuffer stringBuffer;

        /*protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
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
            HttpGet httpGet = new HttpGet(context.getResources() + username + "/" + password);
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return e.getLocalizedMessage();
            }
            return text;
        }*/

        public String doInBackground(Void... inParams) {
            String text;
            try {
                if (params.isEmpty()) {
                    text = "\"message\":\"The parameter is empty\"";
                } else {

                    URL url = new URL((String)params.get(context.getResources().getString(R.string.ws_url)));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod((String)params.get(context.getResources().getString(R.string.ws_method)));
                    conn.setRequestProperty("Content-Type", "application/json");


                    //String input = toJson(params.get(context.getResources().getString(R.string.ws_param)));//"{\"qty\":100,\"name\":\"iPad 4\"}";
                    String input = ReflectionUtil.mapBean2Json((Rest) params.get(context.getResources().getString(R.string.ws_param)));
                    OutputStream os = conn.getOutputStream();
                    os.write(input.getBytes());
                    os.flush();
                    /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                    *//*throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());*//*
                        return "\"message\":\"Failed - HTTP error code. " + conn.getResponseCode() + " \"";
                    }*/
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    StringBuffer outputBuffer = new StringBuffer();
                    String output;
                    System.out.println("Output from Server .... \n");
                    while ((output = br.readLine()) != null) {
                        System.out.println(output);
                        outputBuffer.append(output);
                    }
                    conn.disconnect();
                    text = outputBuffer.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                text = "\"message\":\"" + e.getMessage() + "\"";
            } catch (IOException e) {
                e.printStackTrace();
                text = "\"message\":\"" + e.getMessage() + "\"";
            }
            return text;
        }

        private String toJson(Object paramBean) {

            if (paramBean == null) return "";

            Field[] fields = paramBean.getClass().getFields();
            if (stringBuffer == null) {
                //stringBuffer = new StringBuffer("{\"" + paramBean.getClass().getSimpleName() + "\":{");
                stringBuffer = new StringBuffer("{");
            } else {
                stringBuffer.append("{\"" + paramBean.getClass().getSimpleName() + "\":{");
            }
            try {
                for (Field field : fields) {

                    Object object = field.get(paramBean);
                    if (object instanceof String) {
                        stringBuffer.append("\"" + field.getName() + "\":\"" + field.get(paramBean) + "\",");
                    } else if (object instanceof List) {
                        List<? extends Rest> rests = (List<? extends Rest>) object;
                        if (!rests.isEmpty()) {
                            for (Rest rest : rests) {
                                toJson(rest);
                                stringBuffer.append("}}");
                            }
                        }
                    }
                }
                stringBuffer.append("\"test\":\"test\"}");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        protected void onPostExecute(String results) {
            try {
                JSONObject jObject = new JSONObject(results);
                restCaller.onWebServiceResult(jObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}