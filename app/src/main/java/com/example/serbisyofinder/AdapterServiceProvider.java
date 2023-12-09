package com.example.serbisyofinder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterServiceProvider extends FirebaseRecyclerAdapter<Account, AdapterServiceProvider.myViewHolder> {

    private String serviceName, subService;

    public AdapterServiceProvider(@NonNull FirebaseRecyclerOptions<Account> options, String serviceName, String subService) {
        super(options);
        this.serviceName = serviceName;
        this.subService = subService;
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Account account) {

        holder.providerID.setText(account.getAccID());
        holder.providerName.setText(account.getName());
        holder.providerOccupation.setText(account.getOccupation());
        holder.ratingsCounter.setText(account.getStarRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getRef(position).getKey();
                Intent intent = new Intent(view.getContext(), UserFormBookService.class);

                intent.putExtra("key", key);
                intent.putExtra("accID", key);
                intent.putExtra("providerPhoneNum",account.getPhoneNum());
                intent.putExtra("providerName",account.getName());
                intent.putExtra("providerOccupation",account.getOccupation());
                intent.putExtra("serviceName",serviceName);
                intent.putExtra("address",account.getAddress());
                intent.putExtra("subService",subService);

                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serviceprovider, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView providerID, providerName, providerOccupation, ratingsCounter;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            providerID = (TextView) itemView.findViewById(R.id.providerID);
            providerName = (TextView) itemView.findViewById(R.id.providerName);
            providerOccupation = (TextView) itemView.findViewById(R.id.providerOccupation);
            ratingsCounter = (TextView) itemView.findViewById(R.id.ratingsCounter);
        }
    }
}


