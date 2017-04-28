package com.dealfaro.luca.androidhomephone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "androidhomephone";

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getButton(View V){
        getMessages("abracadabra");
    }

    public void sendButton(View V){
        sendMsg("abracadabra");
    }

    private void sendMsg(final String token) {
        final TextView postText = (TextView)findViewById(R.id.textView);
        final String post_url = "https://luca-ucsc-teaching-backend.appspot.com/hw3/request_via_post";
        String my_url = post_url + "?token=" + URLEncoder.encode(token);
        StringRequest sr = new StringRequest(Request.Method.POST,
                my_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG_TAG, "Got:" + response);
                        String[] result = response.split(Pattern.quote("\""));
                        postText.setText(result[3]);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token", token);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);

    }

    private void getMessages(final String token) {
        final TextView getText = (TextView)findViewById(R.id.textView);
        // Instantiate the RequestQueue.
        String get_url = "https://luca-ucsc-teaching-backend.appspot.com/hw3/request_via_get";

        String my_url = get_url + "?token=" + URLEncoder.encode(token);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, my_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(LOG_TAG, "Received: " + response.toString());
                        // Ok, let's disassemble a bit the json object.
                        try {
                            String result = response.getString("result");
                            getText.setText(result);
                        }catch(JSONException e){
                            Log.d(LOG_TAG, "No JSON get result");
                            getText.setText("No result???");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(LOG_TAG, error.toString());
                    }
                });
        // In some cases, we don't want to cache the request.
        // jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);
    }
}
