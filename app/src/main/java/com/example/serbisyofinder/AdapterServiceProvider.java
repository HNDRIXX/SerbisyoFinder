package com.example.serbisyofinder;

import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterServiceProvider extends FirebaseRecyclerAdapter<Account, AdapterServiceProvider.myViewHolder> {

    ArrayList<String> arrayList = new ArrayList<>();
    private String serviceName, subService;

    public AdapterServiceProvider(@NonNull FirebaseRecyclerOptions<Account> options, String serviceName, String subService ) {
        super(options);
        this.serviceName = serviceName;
        this.subService = subService;
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull Account account) {

       if (serviceName.equals(account.getMainService())){
           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Provider Schedule");
           databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   StringBuilder matchingItems = new StringBuilder();

                   for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                       String providerName = childSnapshot.child("providerName").getValue(String.class);
                       String review = childSnapshot.child("review").getValue(String.class);
                       String rating = childSnapshot.child("rating").getValue(String.class);

                       if (!review.equals("No Review")){
                           String data = review;

                           if (providerName != null && providerName.equals(account.getName())) {
                               matchingItems.append("<i><font color='#FFA500'>&#9733;</font> " + rating).append(" - " + data).append("</i><br>");
                           }
                       }
                   }

                   if (matchingItems.toString().trim().isEmpty() || matchingItems.toString().trim() == null) {
                       holder.reviewContent.setText("None");
                   } else {
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                           holder.reviewContent.setText(Html.fromHtml(matchingItems.toString().trim(), Html.FROM_HTML_MODE_LEGACY));
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {
                   // Handle the error
               }
           });

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
       }else {
           holder.rootView.setLayoutParams(holder.params);
       }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serviceprovider, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView providerID, providerName, providerOccupation, ratingsCounter, reviewContent;
        CardView.LayoutParams params;
        CardView rootView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            params = new CardView.LayoutParams(0, 0);
            rootView = itemView.findViewById(R.id.rootView);

            reviewContent = (TextView) itemView.findViewById(R.id.reviewContent);
            providerID = (TextView) itemView.findViewById(R.id.providerID);
            providerName = (TextView) itemView.findViewById(R.id.providerName);
            providerOccupation = (TextView) itemView.findViewById(R.id.providerOccupation);
            ratingsCounter = (TextView) itemView.findViewById(R.id.ratingsCounter);
        }
    }
}


