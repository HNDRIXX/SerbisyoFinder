package com.example.serbisyofinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterProviders extends FirebaseRecyclerAdapter<Account, AdapterProviders.myViewHolder> {

    public AdapterProviders(@NonNull FirebaseRecyclerOptions<Account> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Account account) {

        if (account.getRole().equals("Service Provider")) {
            holder.nameValue.setText(account.getName());
            holder.addressValue.setText(account.getAddress());
            holder.genderValue.setText(account.getGender());
            holder.occupationValue.setText(account.getOccupation());
        } else {
            holder.rootView.setLayoutParams(holder.params);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_providers, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView nameValue, addressValue, genderValue, occupationValue;
        CardView.LayoutParams params;
        CardView rootView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            params = new CardView.LayoutParams(0, 0);
            rootView = itemView.findViewById(R.id.rootView);

            nameValue = (TextView) itemView.findViewById(R.id.nameValue);
            addressValue = (TextView) itemView.findViewById(R.id.addressValue);
            genderValue = (TextView) itemView.findViewById(R.id.genderValue);
            occupationValue = (TextView) itemView.findViewById(R.id.occupationValue);
        }
    }
}


