package com.phibasis.www.travelyaaridemo;

import android.app.Dialog;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.phibasis.www.travelyaaridemo.Helper.ApiHelper;
import com.phibasis.www.travelyaaridemo.Helper.CustomAdapter;
import com.phibasis.www.travelyaaridemo.Helper.ProjectConfig;
import com.phibasis.www.travelyaaridemo.Helper.VolleyCallback;
import com.phibasis.www.travelyaaridemo.Model.QueryResponse;
import com.phibasis.www.travelyaaridemo.Model.Route;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
    SimpleDateFormat startTimeFormat = new SimpleDateFormat("hh:mm a");

    ListView listView;
    private static CustomAdapter adapter;
    TextView tvTo;
    TextView tvFrom;
    TextView tvDate;
    TextView tvPriceFilter;
    TextView tvTimeFilter;
    TextView tvSeatsFilter;
    ImageView ivPriceArrow;
    ImageView ivTimeArrow;
    ImageView ivSeatsArrow;
    Dialog dialog;

    CheckBox cbAC;
    CheckBox cbNonAC;
    CheckBox cbSleeper;
    CheckBox cbSemiSleeper;
    CheckBox cbVolvo;
    CheckBox cbNonVolvo;



    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lvItems);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvTo = (TextView) findViewById(R.id.tvTo);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvPriceFilter = (TextView) findViewById(R.id.tvPriceFilter);
        tvTimeFilter = (TextView) findViewById(R.id.tvTimeFilter);
        tvSeatsFilter = (TextView) findViewById(R.id.tvSeatsFilter);
        ivPriceArrow = (ImageView) findViewById(R.id.ivPriceArrow);
        ivTimeArrow = (ImageView) findViewById(R.id.ivTimeArrow) ;
        ivSeatsArrow = (ImageView) findViewById(R.id.ivSeatsArrow);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);

                makeApiCall();
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);

        makeApiCall();

        //Creating the dialog
        dialogSetup();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
            }
        });

        tvTimeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag().equals("high"))
                {
                    view.setTag("low");
                    ivTimeArrow.setImageResource(R.drawable.ic_up_arrow);
                    Collections.reverse(ProjectConfig.savedResponse.routes);

                }
                else
                {
                    view.setTag("high");
                    ivTimeArrow.setImageResource(R.drawable.ic_down_arrow);
                    Collections.reverse(ProjectConfig.savedResponse.routes);

                }
                setNewAdapter(ProjectConfig.savedResponse.routes);
            }
        });

        tvPriceFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag().equals("high"))
                {
                    view.setTag("low");
                    ivPriceArrow.setImageResource(R.drawable.ic_up_arrow);
                    Collections.sort(ProjectConfig.savedResponse.routes, new Comparator<Route>() {

                        @Override
                        public int compare(Route route, Route t1) {
                            return route.fare - t1.fare;
                        }
                    });

                }
                else
                {
                    view.setTag("high");
                    ivPriceArrow.setImageResource(R.drawable.ic_down_arrow);
                    Collections.sort(ProjectConfig.savedResponse.routes, new Comparator<Route>() {

                        @Override
                        public int compare(Route route, Route t1) {
                            return t1.fare-route.fare;
                        }
                    });
                }
                setNewAdapter(ProjectConfig.savedResponse.routes);
            }
        });

        tvSeatsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag().equals("high"))
                {
                    view.setTag("low");
                    ivSeatsArrow.setImageResource(R.drawable.ic_up_arrow);
                    Collections.sort(ProjectConfig.savedResponse.routes, new Comparator<Route>() {

                        @Override
                        public int compare(Route route, Route t1) {
                            return route.seats_available - t1.seats_available;
                        }
                    });

                }
                else
                {
                    view.setTag("high");
                    ivSeatsArrow.setImageResource(R.drawable.ic_down_arrow);
                    Collections.sort(ProjectConfig.savedResponse.routes, new Comparator<Route>() {

                        @Override
                        public int compare(Route route, Route t1) {
                            return t1.seats_available-route.seats_available;
                        }
                    });
                }
                setNewAdapter(ProjectConfig.savedResponse.routes);
            }
        });


    }

    public void dialogSetup()
    {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_filter);

        cbAC = (CheckBox) dialog.findViewById(R.id.cbAC);
        cbAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectConfig.filterAc = ((CheckBox) view).isChecked();

            }
        });

        cbNonAC = (CheckBox) dialog.findViewById(R.id.cbNonAC);
        cbNonAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectConfig.filterNonAc = ((CheckBox) view).isChecked();
            }
        });

        cbSleeper = (CheckBox) dialog.findViewById(R.id.cbSleeper);
        cbSleeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectConfig.filterSleeper = ((CheckBox) view).isChecked();
            }
        });

        cbSemiSleeper = (CheckBox) dialog.findViewById(R.id.cbSemiSleeper);
        cbSemiSleeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectConfig.filterSemiSleeper = ((CheckBox) view).isChecked();
            }
        });

        cbVolvo = (CheckBox) dialog.findViewById(R.id.cbVolvo);
        cbVolvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectConfig.filterVolvo = ((CheckBox) view).isChecked();
            }
        });

        cbNonVolvo = (CheckBox) dialog.findViewById(R.id.cbNonVolvo);
        cbNonVolvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectConfig.filterNonVolvo = ((CheckBox) view).isChecked();
            }
        });


        Button btnFilter = (Button) dialog.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFilter();
            }
        });

        Button btnReset = (Button) dialog.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialogSetup();
                ProjectConfig.savedResponse.routes = ProjectConfig.pristineData.routes;
                setNewAdapter(ProjectConfig.savedResponse.routes);
            }
        });

    }

    public void applyFilter()
    {

        if(dialog.isShowing())
        {
            dialog.dismiss();
        }

        ArrayList<Route> filteredResultTotal = new ArrayList<Route>();

        boolean ignoreAC = false;
        boolean ignoreVolvo = false;
        boolean ignoreSleeper = false;


        if(cbAC.isChecked() && cbNonAC.isChecked())
        {
            ignoreAC = true;
        }
        else if(!cbNonAC.isChecked() && !cbAC.isChecked())
        {
           ignoreAC = true;
        }

        if(cbVolvo.isChecked() && cbNonVolvo.isChecked())
        {
            ignoreVolvo = true;
        }
        if(!cbVolvo.isChecked() && !cbNonVolvo.isChecked())
        {
            ignoreVolvo = true;
        }

        if(cbSleeper.isChecked() && cbSemiSleeper.isChecked())
        {
            ignoreSleeper = true;
        }
        if(!cbSleeper.isChecked() && !cbSemiSleeper.isChecked())
        {
            ignoreSleeper = true;
        }


        if(!ignoreAC)
        {
            for (int i=0;i<ProjectConfig.pristineData.routes.size()-1;i++)
            {
                Route route = ProjectConfig.pristineData.routes.get(i);
                if(route.is_ac==cbAC.isChecked())
                {
                    filteredResultTotal.add(route);

                }
            }
        }
        else
        {
            filteredResultTotal = ProjectConfig.pristineData.routes;
        }


        ArrayList<Route> secondFilter = new ArrayList<Route>();

        if(!ignoreVolvo)
        {
            for (int i=0;i<filteredResultTotal.size()-1;i++)
            {
                Route route = filteredResultTotal.get(i);
                if(route.is_volvo==cbVolvo.isChecked())
                {
                    secondFilter.add(route);

                }
            }
        }
        else
        {
            secondFilter = filteredResultTotal;
        }

        ArrayList<Route> thirdFilter = new ArrayList<Route>();

        if(!ignoreSleeper)
        {
            for (int i=0;i<secondFilter.size()-1;i++)
            {
                Route route = secondFilter.get(i);
                if(route.is_sleeper==cbSleeper.isChecked())
                {
                    thirdFilter.add(route);

                }
            }
        }
        else
        {
            thirdFilter = secondFilter;
        }



        setNewAdapter(thirdFilter);

        //ProjectConfig.savedResponse.routes = thirdFilter;



    }



    public void setNewAdapter(ArrayList<Route> route)
    {
        adapter= new CustomAdapter(route,getApplicationContext());
        listView.setAdapter(adapter);
    }

    public void makeApiCall()
    {
        ApiHelper.Call(getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                ProjectConfig.StaticLog(result);
                try
                {

                    QueryResponse returnObj = new Gson().fromJson(result, QueryResponse.class);
                    ProjectConfig.pristineData = returnObj;
                    ProjectConfig.savedResponse = returnObj;

                    tvFrom.setText(returnObj.search_query.to);
                    tvTo.setText(returnObj.search_query.from);

                    SimpleDateFormat actualDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");


                    tvDate.setText(dateFormat.format(actualDateFormat.parse(returnObj.search_query.date)));

                    setNewAdapter(returnObj.routes);

                    if(mSwipeRefreshLayout.isRefreshing())
                    {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }
                catch (Exception e)
                {
                    ProjectConfig.StaticLog(e.getMessage());
                    ProjectConfig.StaticToast(getApplicationContext(),e.getMessage());
                    ProjectConfig.StaticToast(getApplicationContext(),"Something went wrong! Please try again later.");
                }
            }
        });
    }




}
