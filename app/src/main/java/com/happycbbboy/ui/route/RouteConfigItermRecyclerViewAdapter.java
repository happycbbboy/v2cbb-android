package com.happycbbboy.ui.route;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.databinding.FragmentRouteConfItermBinding;
import com.happycbbboy.domain.RouteConfig;

import java.util.List;

public class RouteConfigItermRecyclerViewAdapter extends RecyclerView.Adapter<RouteConfigItermRecyclerViewAdapter.ViewHolder> {

    private final List<RouteConfig> mValues;

    public RouteConfigItermRecyclerViewAdapter(List<RouteConfig> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentRouteConfItermBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(holder.mItem.getName());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public RouteConfig mItem;

        public ViewHolder(FragmentRouteConfItermBinding binding) {
            super(binding.getRoot());
            mNameView = binding.fragmentRouteConfItermName;
        }

    }
}