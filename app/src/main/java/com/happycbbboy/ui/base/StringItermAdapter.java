package com.happycbbboy.ui.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.databinding.FragmentRouteConfItermBinding;
import com.happycbbboy.domain.RouteConfig;
import com.happycbbboy.ui.route.RouteConfigItermRecyclerViewAdapter;

import java.util.List;

public class StringItermAdapter extends RecyclerView.Adapter<RouteConfigItermRecyclerViewAdapter.ViewHolder> {
    List<String> iterms;

    public StringItermAdapter(List<String> iterms) {
        this.iterms = iterms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteConfigItermRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mNameView;
        public ViewHolder(FragmentRouteConfItermBinding binding) {
            super(binding.getRoot());
            mNameView = binding.routeItermTitle;
        }

    }
}
