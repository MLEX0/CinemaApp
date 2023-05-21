package com.kp.cinemaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kp.cinemaapp.R;

import java.util.ArrayList;
import java.util.List;
public class SpinnerBuyTicketAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mData;

    public SpinnerBuyTicketAdapter(Context context, ArrayList<String> data) {
        super(context, R.layout.item_rounded_spinner, data);
        mContext = context;
        mData = data;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_rounded_spinner, parent, false);

        TextView text = view.findViewById(R.id.tvNumber);
        ImageView Row = view.findViewById(R.id.imageViewRow);
        Row.setVisibility(View.GONE);

        text.setTextColor(Color.parseColor("white"));

        view.setBackgroundColor(Color.parseColor("#697BEA"));
        text.setText(mData.get(position));

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_rounded_spinner, parent, false);

        TextView text = view.findViewById(R.id.tvNumber);

        text.setText(mData.get(position));

        return view;
    }
}