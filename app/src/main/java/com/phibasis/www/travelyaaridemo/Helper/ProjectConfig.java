package com.phibasis.www.travelyaaridemo.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.phibasis.www.travelyaaridemo.Model.QueryResponse;
import com.phibasis.www.travelyaaridemo.Model.Route;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ProjectConfig {
    public static String Base_Url = "https://api.myjson.com/bins/7afms";

    public static QueryResponse savedResponse = new QueryResponse();
    public static QueryResponse pristineData = new QueryResponse();

    public static ArrayList<Route> filteredResultTotal = new ArrayList<Route>();



    public static void StaticLog(String logMessage)
    {
        Log.i("kingsukm",logMessage);
    }

    public static void StaticToast(Context applicationContext, String message)
    {
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show();
    }

    public static boolean filterNonAc = false;
    public static boolean filterAc = false;
    public static boolean filterSemiSleeper = false;
    public static boolean filterSleeper = false;
    public static boolean filterNonVolvo = false;
    public static boolean filterVolvo = false;




}
