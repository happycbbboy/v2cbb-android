package com.happycbbboy.ui.route;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.databases.RouteConfigDatabase;
import com.happycbbboy.databases.dao.RouteConfigDao;
import com.happycbbboy.databinding.FragmentRouteConfItermBinding;
import com.happycbbboy.domain.EventBusConstant;
import com.happycbbboy.domain.EventBusMsg;
import com.happycbbboy.domain.RouteConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RouteConfigItermRecyclerViewAdapter extends RecyclerView.Adapter<RouteConfigItermRecyclerViewAdapter.ViewHolder> {

    private final List<RouteConfig> mValues;
    private RouteConfigDao routeConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    public RouteConfigItermRecyclerViewAdapter(List<RouteConfig> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RouteConfigDatabase instance = RouteConfigDatabase.getInstance(parent.getContext());
        routeConfigDao = instance.proxyConfigDao();
        return new ViewHolder(FragmentRouteConfItermBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(holder.mItem.getName());
        holder.routeItermEdit.setOnClickListener(v -> {
            EventBus.getDefault().post(new EventBusConstant(EventBusMsg.ROUTE_SETTING_SUBMIT,holder.mItem.getId()));
        });
        holder.routeItermDelete.setOnClickListener(v -> {
            mDisposable.add(routeConfigDao.delete(holder.mItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Toast.makeText(holder.itemView.getContext(), "route conf delete successful", Toast.LENGTH_SHORT).show();
                            },
                            throwable -> Log.e("ProxyConfigManagerOnClick", "Unable to update username", throwable)));

        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RouteConfig mItem;
        public final TextView mNameView;
        public Button routeItermDelete;
        public Button routeItermEdit;
        public ViewHolder(FragmentRouteConfItermBinding binding) {
            super(binding.getRoot());
            mNameView = binding.routeItermTitle;
            routeItermEdit = binding.routeItermEdit;
            routeItermDelete = binding.routeItermDelete;
        }

    }
}