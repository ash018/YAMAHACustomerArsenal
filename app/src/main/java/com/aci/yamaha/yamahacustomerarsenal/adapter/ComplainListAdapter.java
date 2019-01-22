package com.aci.yamaha.yamahacustomerarsenal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.model.Complain;

import java.util.ArrayList;

/**
 * Created by aburasel on 8/16/2017.
 */


public class ComplainListAdapter extends RecyclerView.Adapter<ComplainListAdapter.MyViewHolder> {
    private ArrayList<Complain> complainArrayList;

    public ComplainListAdapter(ArrayList<Complain> complains) {
        this.complainArrayList = complains;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complain_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Complain complain = complainArrayList.get(position);
        holder.categoryTextView.setText(complain.getCategory());
        holder.messageTextView.setText(complain.getMessage());
        holder.complainDateTextView.setText(complain.getEntryDate());
    }

    @Override
    public int getItemCount() {
        return complainArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView complainDateTextView, messageTextView, categoryTextView;

        public MyViewHolder(View view) {
            super(view);
            complainDateTextView = view.findViewById(R.id.complainDateTextView);
            messageTextView = view.findViewById(R.id.messageTextView);
            categoryTextView = view.findViewById(R.id.categoryTextView);
        }
    }
}
