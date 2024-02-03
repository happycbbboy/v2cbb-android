package com.happycbbboy.ui.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.databinding.StringItermBinding;

import java.util.List;

public class StringItermAdapter extends RecyclerView.Adapter<StringItermAdapter.ViewHolder> {
    List<String> iterms;

    public StringItermAdapter(List<String> iterms) {
        this.iterms = iterms;
    }

    public List<String> getIterms() {
        return iterms;
    }

    @NonNull
    @Override
    public StringItermAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StringItermAdapter.ViewHolder(StringItermBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StringItermAdapter.ViewHolder holder, int position) {
        String text = iterms.get(position);
        holder.mNameView.setText(text);
        holder.binding.stringItermDelete.setOnClickListener(v -> {
            synchronized (holder){
                iterms.remove(text);
                notifyItemRemoved(position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return iterms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        StringItermBinding  binding;
        public final TextView mNameView;
        public ViewHolder(StringItermBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            mNameView = binding.stringItermTitle;
        }
    }
}
