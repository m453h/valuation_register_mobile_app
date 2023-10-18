package com.aru.valuationregister.MenuActivities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aru.valuationregister.R;

import java.util.ArrayList;


/**
 * Created by michael.nkotagu on 6/18/2015.
 */

public class MenuMyAdapter extends RecyclerView.Adapter<MenuMyAdapter.MyViewHolder> {

    private final ArrayList<MenuData> menuDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewDescription;
        ImageView imageViewIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public MenuMyAdapter(ArrayList<MenuData> items) {
        this.menuDataSet = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(MainMenuActivity.myOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewDescription = holder.textViewDescription;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(menuDataSet.get(listPosition).getName());
        textViewDescription.setText(menuDataSet.get(listPosition).getDescription());
        imageView.setImageResource(menuDataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        return menuDataSet.size();
    }
}
