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

public class AdapterSchedule extends FirebaseRecyclerAdapter<Schedule, AdapterSchedule.myViewHolder> {

    public AdapterSchedule(@NonNull FirebaseRecyclerOptions<Schedule> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Schedule schedule) {

        holder.providerScheduleDate.setText(schedule.getProviderSchedule());

        if(schedule.getFlag().equals("0")){
            holder.rootView.setLayoutParams(holder.params);
        } else {
            String inputDate = schedule.getProviderSchedule();
            String[] dateParts = inputDate.split("[, ]");
            if (dateParts.length >= 3) {
                String month = dateParts[0];
                String day = dateParts[1];

                String year = dateParts[dateParts.length - 1];

                // Set the values in your TextViews
                holder.providerScheduleMonth.setText(month);
                holder.providerScheduleDay.setText(day);
                holder.providerScheduleYear.setText(year);
            }
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String key = getRef(position).getKey();
//                Intent intent = new Intent(view.getContext(), UserFormBookService.class);
//
//                intent.putExtra("key", key);
//                intent.putExtra("accID", schedule.getAccID());
//                intent.putExtra("providerSchedule", schedule.getProviderSchedule());
//                intent.putExtra("clientAddress", schedule.getClientAddress());
//
//                view.getContext().startActivity(intent);
//            }
//        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_providersched, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView providerScheduleMonth, providerScheduleDay, providerScheduleYear, providerScheduleDate;
        TextView locationValueText;
        CardView.LayoutParams params;
        CardView rootView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            params = new CardView.LayoutParams(0, 0);
            rootView = itemView.findViewById(R.id.rootView);

            providerScheduleMonth = (TextView) itemView.findViewById(R.id.providerScheduleMonth);
            providerScheduleDay = (TextView) itemView.findViewById(R.id.providerScheduleDay);
            providerScheduleYear = (TextView) itemView.findViewById(R.id.providerScheduleYear);
            providerScheduleDate = (TextView) itemView.findViewById(R.id.providerScheduleDate);
        }
    }
}


