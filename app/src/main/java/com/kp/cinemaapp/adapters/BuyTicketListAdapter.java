package com.kp.cinemaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kp.cinemaapp.R;
import com.kp.cinemaapp.model.Cinema;
import com.kp.cinemaapp.model.Ticket;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BuyTicketListAdapter extends ArrayAdapter<Ticket> {
    public BuyTicketListAdapter(@NonNull Context context, ArrayList<Ticket> ticketArrayList) {
        super(context, R.layout.item_list_buy_ticket, (List<Ticket>) ticketArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ticket ticket = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_list_buy_ticket, parent, false);
        }

        ArrayList<String> rows = new ArrayList<>();
        ArrayList<String> places = new ArrayList<>();

        Spinner SpinnerRow = convertView.findViewById(R.id.spinnerTicketRow);
        Spinner SpinnerPlace = convertView.findViewById(R.id.spinnerTicketPlace);

        SpinnerBuyTicketAdapter rowsAdapter = new SpinnerBuyTicketAdapter(getContext(), rows);
        SpinnerBuyTicketAdapter placesAdapter = new SpinnerBuyTicketAdapter(getContext(), places);

        SpinnerRow.setAdapter(rowsAdapter);
        SpinnerPlace.setAdapter(placesAdapter);

        return convertView;
    }
}
//extends ArrayAdapter<Ticket>

/*            Spinner mySpinner = findViewById(R.id.my_spinner);
            ArrayList<String> values = new ArrayList<>();
            values.add("1");
            values.add("2");
            SpinnerBuyTicketAdapter adapter = new SpinnerBuyTicketAdapter(this, values);
            mySpinner.setAdapter(adapter);
            */