package com.phibasis.www.travelyaaridemo.Helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.phibasis.www.travelyaaridemo.Model.Route;
import com.phibasis.www.travelyaaridemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends ArrayAdapter<Route>{

    private ArrayList<Route> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvAmenities;
        TextView tvOperator;
        TextView tvTime;
        TextView tvPrice;
        TextView tvSeats;
    }



    public CustomAdapter(ArrayList<Route> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Route dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.tvAmenities = (TextView) convertView.findViewById(R.id.tvAmenities);
            viewHolder.tvOperator = (TextView) convertView.findViewById(R.id.tvOperator);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvSeats = (TextView) convertView.findViewById(R.id.tvSeats);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;


        viewHolder.tvOperator.setText(dataModel.operator);
        SimpleDateFormat actualDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        SimpleDateFormat startTimeFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat endDateFormat = new SimpleDateFormat("hh:mm a, dd MMM");

        try
        {

            String time = startTimeFormat.format(actualDateFormat.parse(dataModel.trip_start_time))+"  -----  "+endDateFormat.format(actualDateFormat.parse(dataModel.trip_end_time));
            viewHolder.tvTime.setText(time);
        }
        catch (Exception e)
        {
            ProjectConfig.StaticLog(e.getMessage());
        }


        String amenities = "";
        if(dataModel.is_ac)
        {
            amenities = "A/C";
        }
        else
        {
            amenities = "Non A/C";
        }

        amenities += dataModel.is_sleeper ? ",Sleeper":",Seater";
        amenities += dataModel.is_volvo ? ",Volvo":"";


        viewHolder.tvAmenities.setText(amenities);

        viewHolder.tvPrice.setText("₹"+dataModel.fare.toString());
        viewHolder.tvSeats.setText(dataModel.seats_available.toString()+" Seat(s)");
        // Return the completed view to render on screen
        return convertView;
    }



}
