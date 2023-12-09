package com.example.serbisyofinder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterUserBookings extends FirebaseRecyclerAdapter<Schedule, AdapterUserBookings.myViewHolder> {

    public AdapterUserBookings(@NonNull FirebaseRecyclerOptions<Schedule> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Schedule schedule) {

        if(schedule.getFlag().equals("0")){
            holder.rootView.setLayoutParams(holder.params);
        } else {
            holder.mainServiceValue.setText(schedule.getMainService());
            holder.subServiceValue.setText(schedule.getSubService());
            holder.dateServiceValue.setText(schedule.getProviderSchedule());
            holder.serviceProviderValue.setText(schedule.getProviderName());
            holder.priceValue.setText(schedule.getPrice());
            holder.statusValue.setText(schedule.getStatus());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getRef(position).getKey();
                Intent intent = new Intent(view.getContext(), UserBookingsPanel.class);

                intent.putExtra("key", key);
                intent.putExtra("scheduleID", schedule.getScheduleID());
                intent.putExtra("providerID", schedule.getAccID());
                intent.putExtra("clientID", schedule.getClientID());
                intent.putExtra("clientName", schedule.getClientName());
                intent.putExtra("providerPhoneNum", schedule.getProviderPhoneNum());
                intent.putExtra("providerName", schedule.getProviderName());
                intent.putExtra("mainService", schedule.getMainService());
                intent.putExtra("subService", schedule.getSubService());
                intent.putExtra("price", schedule.getPrice());
                intent.putExtra("status", schedule.getStatus());

                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userbookings, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView mainServiceValue, subServiceValue, dateServiceValue, serviceProviderValue, priceValue, statusValue;
        CardView.LayoutParams params;
        CardView rootView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            params = new CardView.LayoutParams(0, 0);
            rootView = itemView.findViewById(R.id.rootView);

            mainServiceValue = (TextView) itemView.findViewById(R.id.mainServiceValue);
            subServiceValue = (TextView) itemView.findViewById(R.id.subServiceValue);
            dateServiceValue = (TextView) itemView.findViewById(R.id.dateServiceValue);
            serviceProviderValue = (TextView) itemView.findViewById(R.id.serviceProviderValue);
            priceValue = (TextView) itemView.findViewById(R.id.priceValue);
            statusValue = (TextView) itemView.findViewById(R.id.statusValue);
        }
    }
}


