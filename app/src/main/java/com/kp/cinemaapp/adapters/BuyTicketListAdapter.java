package com.kp.cinemaapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kp.cinemaapp.Constants;
import com.kp.cinemaapp.R;
import com.kp.cinemaapp.model.Cinema;
import com.kp.cinemaapp.model.Hall;
import com.kp.cinemaapp.model.HallRowPlace;
import com.kp.cinemaapp.model.Schedule;
import com.kp.cinemaapp.model.Ticket;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BuyTicketListAdapter extends ArrayAdapter<Ticket> {

    TextView TextRows;
    TextView TextPlace;

    CheckBox cbSelectTicket;

    Integer GlobalPosition;


    public BuyTicketListAdapter(@NonNull Context context, ArrayList<Ticket> ticketArrayList) {
        super(context, R.layout.item_list_buy_ticket, (List<Ticket>) ticketArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        GlobalPosition = position;
        Ticket ticket = getItem(GlobalPosition);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_list_buy_ticket, parent, false);
        }


        TextRows = convertView.findViewById(R.id.textViewRowText);
        TextPlace = convertView.findViewById(R.id.textViewPlaceText);
        cbSelectTicket = convertView.findViewById(R.id.checkBoxSelectPlace);

        TextRows.setText(ticket.ticketPlace.row);
        TextPlace.setText(ticket.ticketPlace.place);

        cbSelectTicket.setChecked(ticket.ticketPlace.isBusy);

        cbSelectTicket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Constants.selectedTickets.add(ticket);
                } else {
                    Constants.selectedTickets.remove(ticket);
                }

                UpdateCost();
            }
        });

        return convertView;
    }
    int NewCost;
    public void UpdateCost(){
        NewCost = 0;
        TextView totalCostTextView = (TextView) ((Activity) getContext()).findViewById(R.id.textViewAllCost);
        for(Ticket ticket : Constants.selectedTickets){
            assert ticket != null;
            NewCost += Integer.parseInt(ticket.getThisSchedule().cost);
        }
        totalCostTextView.setText(NewCost + "р");
    }

}