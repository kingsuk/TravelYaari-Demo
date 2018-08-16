package com.phibasis.www.travelyaaridemo.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public final class ApiHelper {


    public static void Call(final Context context,final VolleyCallback callback)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ProjectConfig.Base_Url;

        // Request a string response from the provided URL.
        StringRequest stringRequest = prepareVolleyCallBack(context,url,callback);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public static StringRequest prepareVolleyCallBack(final Context context,String url,final VolleyCallback callback)
    {
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Something went wrong! Please try again.",Toast.LENGTH_SHORT).show();
                ProjectConfig.StaticLog(error.getMessage());

            }
        });
    }


}
