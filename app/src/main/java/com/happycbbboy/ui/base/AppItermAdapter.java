package com.happycbbboy.ui.base;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.R;
import com.happycbbboy.databinding.AppItermBinding;
import com.happycbbboy.ui.selector.SpinnerImageTextAdapter;
import com.happycbbboy.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class AppItermAdapter extends RecyclerView.Adapter<AppItermAdapter.ViewHolder> {

    List<AppUtils.AppInfo> allApps;

    /*    List<String> includePackageName;
        List<String> excludePackageName;

        public List<String> getIncludePackageName() {
            return includePackageName;
        }

        public List<String> getExcludePackageName() {
            return excludePackageName;
        }*/
    public List<AppUtils.AppInfo> getAllApps() {
        return allApps;
    }

    public AppItermAdapter(List<AppUtils.AppInfo> allApps) {
        this.allApps = allApps;
//        this.includePackageName = includePackageName;
//        this.excludePackageName = excludePackageName;
    }

    @NonNull
    @Override
    public AppItermAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppItermAdapter.ViewHolder(AppItermBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppItermAdapter.ViewHolder holder, int position) {
        Log.i("AppItermAdapter","onBindViewHolder accept");

        AppUtils.AppInfo appInfo = allApps.get(position);
        holder.binding.appItermIcon.setImageDrawable(appInfo.getAppIcon());
        holder.binding.appNameTextView.setText(appInfo.getAppName());
        holder.binding.packageNameTextView.setText(appInfo.getPackageName());
        // 使用 ArrayAdapter 设置 Spinner 的选项
        SpinnerImageTextAdapter spinnerImageTextAdapter = new SpinnerImageTextAdapter(holder.itemView.getContext(), R.layout.selector_image_text_iterm, selectorImageTextIterms);
        spinnerImageTextAdapter.setDropDownViewResource(R.layout.selector_image_text_iterm);
        holder.binding.routeAppItermSpinner.setAdapter(spinnerImageTextAdapter);
        holder.binding.routeAppItermSpinner.setSelection(appInfo.getPolicy());
        holder.binding.routeAppItermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                SpinnerImageTextAdapter.SelectorImageTextIterm selectedIterm = selectorImageTextIterms.get(position);
                appInfo.setPolicy(selectedIterm.getPolicy());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
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
    }

    static List<SpinnerImageTextAdapter.SelectorImageTextIterm> selectorImageTextIterms = new ArrayList<>();

    static {
        selectorImageTextIterms.add(new SpinnerImageTextAdapter.SelectorImageTextIterm("默认", R.drawable.grey_dot, AppUtils.AppInfo.NORMAL));
        selectorImageTextIterms.add(new SpinnerImageTextAdapter.SelectorImageTextIterm("放行", R.drawable.red_dot, AppUtils.AppInfo.FREEE));
        selectorImageTextIterms.add(new SpinnerImageTextAdapter.SelectorImageTextIterm("拦截", R.drawable.gree_dot, AppUtils.AppInfo.TUNNEL));
    }
}
