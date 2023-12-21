package com.example.serbisyofinder;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterMessages extends FirebaseRecyclerAdapter<Messages, AdapterMessages.myViewHolder> {

    private String accID;

    public AdapterMessages(@NonNull FirebaseRecyclerOptions<Messages> options, String accID) {
        super(options);
        this.accID = accID;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Messages messages) {
        if (accID.equals(messages.getSenderID())) {
            holder.leftPic.setVisibility(View.GONE);
            holder.rightPic.setVisibility(View.VISIBLE);

            holder.messageView.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.baseOrange2));

            holder.dateTimeValue.setGravity(Gravity.RIGHT);
            holder.dateTimeValue.setTextColor(holder.itemView.getResources().getColor(R.color.white));

            holder.messageValue.setGravity(Gravity.RIGHT);
            holder.messageValue.setTextColor(holder.itemView.getResources().getColor(R.color.white));

            holder.dateTimeValue.setText(messages.getSentDate() + " / " + messages.getSentTime());
            holder.messageValue.setText(messages.getMessage());

            holder.youNameValue.setText("Me");
            holder.youNameValue.setGravity(Gravity.RIGHT);
            holder.youNameValue.setTextColor(holder.itemView.getResources().getColor(R.color.white));

            return;
        }

        if (!accID.equals(messages.getSenderID())) {
            holder.rightPic.setVisibility(View.GONE);
            holder.leftPic.setVisibility(View.VISIBLE);

            holder.messageView.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.grayed));

            holder.dateTimeValue.setGravity(Gravity.START);
            holder.dateTimeValue.setTextColor(holder.itemView.getResources().getColor(R.color.darkGray));

            holder.messageValue.setGravity(Gravity.START);
            holder.messageValue.setTextColor(holder.itemView.getResources().getColor(R.color.darkGray));

            holder.youNameValue.setGravity(Gravity.START);
            holder.youNameValue.setTextColor(holder.itemView.getResources().getColor(R.color.darkGray));

            holder.dateTimeValue.setText(messages.getSentDate() + " / " + messages.getSentTime());
            holder.messageValue.setText(messages.getMessage());
            holder.youNameValue.setText(messages.getSenderName());

            return;
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView youNameValue, messageValue, dateTimeValue;
        ImageView leftPic, rightPic;
        RelativeLayout messageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            messageView = (RelativeLayout) itemView.findViewById(R.id.messageView);
            leftPic = (ImageView) itemView.findViewById(R.id.leftPic);
            rightPic = (ImageView) itemView.findViewById(R.id.rightPic);
            dateTimeValue = (TextView) itemView.findViewById(R.id.dateTimeValue);
            youNameValue = (TextView) itemView.findViewById(R.id.youNameValue);
            messageValue = (TextView) itemView.findViewById(R.id.messageValue);
        }
    }
}


