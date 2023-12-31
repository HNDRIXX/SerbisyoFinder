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

public class AdapterBookingRecords extends FirebaseRecyclerAdapter<Schedule, AdapterBookingRecords.myViewHolder> {

    public AdapterBookingRecords(@NonNull FirebaseRecyclerOptions<Schedule> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Schedule schedule) {

        if(schedule.getFlag().equals("1") || schedule.getFlag().equals("2")){
            holder.rootView.setLayoutParams(holder.params);
        } else {
            holder.mainServiceValue.setText(schedule.getMainService());
            holder.subServiceValue.setText(schedule.getSubService());
            holder.dateServiceValue.setText(schedule.getProviderSchedule());
            holder.clientValue.setText(schedule.getClientName());
            holder.priceValue.setText(schedule.getPrice());
            holder.statusValue.setText(schedule.getStatus());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getRef(position).getKey();
                Intent intent = new Intent(view.getContext(), ProviderBookingRecordsPanel.class);

                intent.putExtra("key", key);
                intent.putExtra("clientName", schedule.getClientName());
                intent.putExtra("mainService", schedule.getMainService());
                intent.putExtra("subService", schedule.getSubService());
                intent.putExtra("price", schedule.getPrice());
                intent.putExtra("status", schedule.getStatus());
                intent.putExtra("rating", schedule.getRating());
                intent.putExtra("review", schedule.getReview());

                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pendingbookings, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView mainServiceValue, subServiceValue, dateServiceValue, clientValue, priceValue, statusValue;
        CardView.LayoutParams params;
        CardView rootView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            params = new CardView.LayoutParams(0, 0);
            rootView = itemView.findViewById(R.id.rootView);

            mainServiceValue = (TextView) itemView.findViewById(R.id.mainServiceValue);
            subServiceValue = (TextView) itemView.findViewById(R.id.subServiceValue);
            dateServiceValue = (TextView) itemView.findViewById(R.id.dateServiceValue);
            clientValue = (TextView) itemView.findViewById(R.id.clientValue);
            priceValue = (TextView) itemView.findViewById(R.id.priceValue);
            statusValue = (TextView) itemView.findViewById(R.id.statusValue);
        }
    }
}


