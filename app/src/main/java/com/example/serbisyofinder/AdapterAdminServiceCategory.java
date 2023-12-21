package com.example.serbisyofinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterAdminServiceCategory extends FirebaseRecyclerAdapter<ServiceCategory, AdapterAdminServiceCategory.myViewHolder> {

    public AdapterAdminServiceCategory(@NonNull FirebaseRecyclerOptions<ServiceCategory> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder (@NonNull myViewHolder holder, int position, @NonNull ServiceCategory serviceCategory) {

        Context context = holder.itemView.getContext();

        holder.serviceID.setText(serviceCategory.getServiceID());
        holder.serviceName.setText(serviceCategory.getServiceName());

        Drawable iconWind = holder.itemView.getContext().getResources().getDrawable(R.drawable.icon_wind);
        Drawable iconHammer = holder.itemView.getContext().getResources().getDrawable(R.drawable.icon_hammer);
        Drawable iconPlug = holder.itemView.getContext().getResources().getDrawable(R.drawable.icon_plug);
        Drawable iconBroom = holder.itemView.getContext().getResources().getDrawable(R.drawable.icon_broom);
        Drawable iconPipe = holder.itemView.getContext().getResources().getDrawable(R.drawable.icon_pipe);
        Drawable iconTools = holder.itemView.getContext().getResources().getDrawable(R.drawable.icon_tools);

        if ("Air Conditioning and Cooling Services".equals(holder.serviceName.getText().toString().trim())) {
            Drawable drawable = iconWind.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.baseOrange), PorterDuff.Mode.SRC_IN);
            holder.serviceIcon.setImageDrawable(drawable);
        } else if ("Carpentry and Handyman Services".equals(holder.serviceName.getText().toString().trim())) {
            Drawable drawable = iconHammer.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.baseOrange), PorterDuff.Mode.SRC_IN);
            holder.serviceIcon.setImageDrawable(drawable);
        } else if ("Electrical Repairs and Installation".equals(holder.serviceName.getText().toString().trim())) {
            Drawable drawable = iconPlug.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.baseOrange), PorterDuff.Mode.SRC_IN);
            holder.serviceIcon.setImageDrawable(drawable);
        } else if ("House Cleaning and Maid Services".equals(holder.serviceName.getText().toString().trim())) {
            Drawable drawable = iconBroom.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.baseOrange), PorterDuff.Mode.SRC_IN);
            holder.serviceIcon.setImageDrawable(drawable);
        } else if ("Plumbing Services".equals(holder.serviceName.getText().toString().trim())) {
            Drawable drawable = iconPipe.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.baseOrange), PorterDuff.Mode.SRC_IN);
            holder.serviceIcon.setImageDrawable(drawable);
        } else if ("House Painting".equals(holder.serviceName.getText().toString().trim())) {
            Drawable drawable = iconTools.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.baseOrange), PorterDuff.Mode.SRC_IN);
            holder.serviceIcon.setImageDrawable(drawable);
        }

        else if (serviceCategory.getServiceImg() != null) {
            RequestOptions requestOptions = new RequestOptions();

            Glide.with(holder.serviceIcon.getContext())
                    .load(serviceCategory.getServiceImg())
                    .apply(requestOptions.placeholder(iconTools)
                            .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)) // Set the size you need here
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (resource instanceof BitmapDrawable) {
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                if (bitmap.hasAlpha()) {
                                    holder.serviceIcon.setColorFilter(ContextCompat.getColor(holder.serviceIcon.getContext(), R.color.baseOrange), PorterDuff.Mode.SRC_IN);
                                }
                            }
                            return false;
                        }
                    })
                    .into(holder.serviceIcon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getRef(position).getKey();
                Intent intent = new Intent(view.getContext(), AdminSubService.class);

                intent.putExtra("key", key);
                intent.putExtra("serviceID", serviceCategory.getServiceID());
                intent.putExtra("serviceName",serviceCategory.getServiceName());
                intent.putExtra("subService1",serviceCategory.getSubService1());
                intent.putExtra("subService2",serviceCategory.getSubService2());
                intent.putExtra("subService3",serviceCategory.getSubService3());
                intent.putExtra("subService4",serviceCategory.getSubService4());
                intent.putExtra("subService5",serviceCategory.getSubService5());

                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicecategory, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView serviceID, serviceName;
        ImageView serviceIcon;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceIcon = (ImageView) itemView.findViewById(R.id.serviceIcon);
            serviceID = (TextView) itemView.findViewById(R.id.serviceID);
            serviceName = (TextView) itemView.findViewById(R.id.serviceName);
        }
    }
}

