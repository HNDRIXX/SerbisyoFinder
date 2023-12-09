package com.example.serbisyofinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterMessages extends FirebaseRecyclerAdapter<Messages, AdapterMessages.myViewHolder> {

    public AdapterMessages(@NonNull FirebaseRecyclerOptions<Messages> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Messages messages) {
        holder.messageValue.setText(messages.getMessage());
        holder.youNameValue.setText(messages.getSenderName());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView youNameValue, messageValue;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            youNameValue = (TextView) itemView.findViewById(R.id.youNameValue);
            messageValue = (TextView) itemView.findViewById(R.id.messageValue);
        }
    }
}


