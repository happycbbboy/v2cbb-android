package com.happycbbboy.ui.base;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.databinding.AppItermBinding;
import com.happycbbboy.utils.AppUtils;

import java.util.List;

public class AppItermAdapter extends RecyclerView.Adapter<AppItermAdapter.ViewHolder> {

    List<AppUtils.AppInfo> allApps;
    private static LayoutInflater inflater;

    public List<AppUtils.AppInfo> getAllApps() {
        return allApps;
    }

    public AppItermAdapter(Context context, List<AppUtils.AppInfo> allApps) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        this.allApps = allApps;
//        this.includePackageName = includePackageName;
//        this.excludePackageName = excludePackageName;
    }

    @NonNull
    @Override
    public AppItermAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("AppItermAdapter", "onCreateViewHolder accept");
        return new ViewHolder(AppItermBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppItermAdapter.ViewHolder holder, int position) {
        Log.i("AppItermAdapter", "onBindViewHolder accept:" + position);
        AppUtils.AppInfo appInfo = allApps.get(position);
        holder.bind(appInfo);
       /* holder.binding.appItermIcon.setImageDrawable(appInfo.getAppIcon());
        holder.binding.appNameTextView.setText(appInfo.getAppName());
        holder.binding.packageNameTextView.setText(appInfo.getPackageName());
        holder.binding.appItermCheckbox.setChecked(appInfo.getCheck());
        // 设置CheckBox状态变化的监听器
        holder.binding.appItermCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    appInfo.setCheck(true);
                } else {
                    appInfo.setCheck(false);
                }
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return allApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppItermBinding binding;

        public ViewHolder(AppItermBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull AppUtils.AppInfo appInfo) {
//            if (appInfo.getIniTial()) {
//                binding.appItermCheckbox.setChecked(appInfo.getCheck());
//                return;
//            }
            // 在这里将数据绑定到视图上
            binding.appItermIcon.setImageDrawable(appInfo.getAppIcon());
            binding.appNameTextView.setText(appInfo.getAppName());
            binding.packageNameTextView.setText(appInfo.getPackageName());
            binding.appItermCheckbox.setChecked(appInfo.getCheck());

            // 设置CheckBox状态变化的监听器
            binding.appItermCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        appInfo.setCheck(true);
                    } else {
                        appInfo.setCheck(false);
                    }
                }
            });
//            appInfo.setIniTial(true);
        }
    }

}
