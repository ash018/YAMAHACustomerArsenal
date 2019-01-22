package com.aci.yamaha.yamahacustomerarsenal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aci.yamaha.yamahacustomerarsenal.R;
import com.aci.yamaha.yamahacustomerarsenal.model.Service;

import java.util.List;

public class DisplayServiceAdapter extends ArrayAdapter<Service> {
    private final Context context;
    private final List<Service> values;
    private final TextView mFooter;
    int serviceStatus = 0;

    public DisplayServiceAdapter(Context context,
                                 TextView footer, List<Service> values, int serviceStatus) {
        super(context, R.layout.row_service, values);
        this.context = context;
        this.values = values;
        this.mFooter = footer;
        this.serviceStatus = serviceStatus;
    }

    public void notifyNoMoreItems() {
        mFooter.setText("No more Items");
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        final ViewGroup p;
        p = parent;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.row_service, null);

            final ViewHolder holder = new ViewHolder();

            holder.serviceTextView = (TextView) view
                    .findViewById(R.id.serviceTextView);
            holder.serviceReplyTextView = (TextView) view
                    .findViewById(R.id.serviceReplyTextView);
            holder.ratingBar = (RatingBar) view
                    .findViewById(R.id.ratingBar);

            holder.serviceTextView.setTag(values.get(position));
            holder.ratingBar.setTag(position);

            holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                    Service service = (Service) holder.serviceTextView.getTag();
                    service.setRating(String.valueOf(rating));
                }
            });

            view.setTag(holder);
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).serviceTextView.setTag(values.get(position));

        }
        ViewHolder holder = (ViewHolder) view.getTag();

        Service service = (Service) holder.serviceTextView.getTag();
        if (serviceStatus != 2) {
            holder.ratingBar.setVisibility(View.GONE);
        } else {
            holder.ratingBar.setVisibility(View.VISIBLE);
        }
        holder.serviceTextView.setText(service.getMessage());
        holder.serviceReplyTextView.setText(service.getReplyMessage());
        holder.ratingBar.setRating(Float.parseFloat(service.getRating()));

        return view;
    }

    static class ViewHolder {
        TextView serviceTextView;
        TextView serviceReplyTextView;
        RatingBar ratingBar;
    }
}
