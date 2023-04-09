package com.kp.cinemaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kp.cinemaapp.R;
import com.kp.cinemaapp.model.Cinema;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CinemaListAdapter extends ArrayAdapter<Cinema> {

    public CinemaListAdapter(@NonNull Context context, ArrayList<Cinema> cinemaArrayList) {
        super(context, R.layout.item_list_cinema, (List<Cinema>) cinemaArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Cinema cinema = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_list_cinema, parent, false);
        }

        ImageView cinemaImage = convertView.findViewById(R.id.imageViewCinema);

        Picasso.get().load(cinema.imagePath).into(cinemaImage);

        return convertView;
    }
}