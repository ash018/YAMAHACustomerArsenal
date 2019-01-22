package com.aci.yamaha.yamahacustomerarsenal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.model.Inquiry;

import java.util.ArrayList;

/**
 * Created by aburasel on 8/16/2017.
 */


public class InquiryListAdapter extends RecyclerView.Adapter<InquiryListAdapter.MyViewHolder> {
    private ArrayList<Inquiry> inquiryList;

    public InquiryListAdapter(ArrayList<Inquiry> inquiryList) {
        this.inquiryList = inquiryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inquiry_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Inquiry inquiry = inquiryList.get(position);
        holder.titleTextView.setText(inquiry.getTitle());
        holder.messageTextView.setText(inquiry.getMessage());
        holder.inquiryDateTextView.setText(inquiry.getEntryDate());
    }

    @Override
    public int getItemCount() {
        return inquiryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView inquiryDateTextView, messageTextView, titleTextView;

        public MyViewHolder(View view) {
            super(view);
            inquiryDateTextView = view.findViewById(R.id.inquiryDateTextView);
            messageTextView = view.findViewById(R.id.messageTextView);
            titleTextView = view.findViewById(R.id.titleTextView);
        }
    }
}
