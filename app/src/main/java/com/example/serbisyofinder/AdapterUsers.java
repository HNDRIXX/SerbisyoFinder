package com.example.serbisyofinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterUsers extends FirebaseRecyclerAdapter<Account, AdapterUsers.myViewHolder> {

    public AdapterUsers(@NonNull FirebaseRecyclerOptions<Account> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Account account) {

        holder.nameValue.setText(account.getName());
        holder.addressValue.setText(account.getAddress());
        holder.genderValue.setText(account.getGender());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView nameValue, addressValue, genderValue;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            nameValue = (TextView) itemView.findViewById(R.id.nameValue);
            addressValue = (TextView) itemView.findViewById(R.id.addressValue);
            genderValue = (TextView) itemView.findViewById(R.id.genderValue);
        }
    }
}


