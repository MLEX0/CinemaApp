package com.kp.cinemaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kp.cinemaapp.R;
import com.kp.cinemaapp.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketListAdapter extends ArrayAdapter<Ticket> {

    public TicketListAdapter(@NonNull Context context, ArrayList<Ticket> ticketArrayList) {
        super(context, R.layout.item_list_ticket, (List<Ticket>) ticketArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ticket ticket = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_list_ticket, parent, false);
        }

        TextView ticketDate = convertView.findViewById(R.id.textViewDateText);
        TextView ticketTime = convertView.findViewById(R.id.textViewTimeText);
        TextView ticketHall = convertView.findViewById(R.id.textViewHallText);
        TextView ticketRow = convertView.findViewById(R.id.textViewTicketRowText);
        TextView ticketPlace = convertView.findViewById(R.id.textViewTicketPlaceText);

        ticketDate.setText(ticket.getThisSchedule().date.replace('-', '.'));
        ticketTime.setText(ticket.getThisSchedule().time);
        ticketHall.setText(ticket.getThisSchedule().hall.hallNumber);
        ticketRow.setText(ticket.ticketPlace.row);
        ticketPlace.setText(ticket.ticketPlace.place);

        return convertView;
    }
}


