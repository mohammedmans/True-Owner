package com.example.mo.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FaceRecognition extends Service {


    RequestQueue queue  ;
    final String url = "http://api.androidhive.info/volley/person_object.json";
    String result;
    Context ctx ;


    public FaceRecognition(Context ctx) {
        this.ctx = ctx ;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        vollyRequest();
        return null;
    }


    void vollyRequest() {
        queue = Volley.newRequestQueue(getApplicationContext());
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest request = new StringRequest(Request.Method.GET,url,new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject obj = new JSONObject(response);
                    result = obj.toString();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            protected java.util.Map<String, String> getParams() throws AuthFailureError
            {
                java.util.Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(request);
    }
}
