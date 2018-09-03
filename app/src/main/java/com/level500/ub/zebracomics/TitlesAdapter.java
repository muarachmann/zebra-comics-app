package com.level500.ub.zebracomics;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.MyViewHolder> {
    private Context context;
    private List<Titles> cartList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }


    public TitlesAdapter(Context context, List<Titles> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.titles_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Titles item = cartList.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        Glide.with(context)
                .load(item.getThumbnail())
                .into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == getItemCount()-1){
                    Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", item.getId()); // Put anything what you want

                    Toast.makeText(context, item.getId(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }



}