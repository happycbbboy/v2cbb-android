package com.happycbbboy.ui.route;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.databases.RouteConfigDatabase;
import com.happycbbboy.databases.dao.RouteConfigDao;
import com.happycbbboy.databinding.FragmentRouteConfListBinding;
import com.happycbbboy.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class RouteConfListFragment extends Fragment {

    private RouteConfigDao routeConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private FragmentRouteConfListBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RouteConfigDatabase instance = RouteConfigDatabase.getInstance(getActivity().getApplication());
        routeConfigDao = instance.proxyConfigDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Log.i("async AppUtils", "start load all app");
                for (int i = 0; i < 10; i++) {
                    List<AppUtils.AppInfo> allInstalledApps = AppUtils.getAllInstalledApps(requireContext(), "", new ArrayList<>(0));
                    if (allInstalledApps != null && allInstalledApps.size() > 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("getAllInstalledApps", "getAllInstalledApps sleep err:" + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRouteConfListBinding.inflate(inflater, container, false);
        RecyclerView root = (RecyclerView) binding.getRoot();

        mDisposable.add(routeConfigDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                            root.setAdapter(new RouteConfigItermRecyclerViewAdapter(list));
                        },
                        throwable -> Log.e("onViewCreated", "proxyConfigDao get all:", throwable)));

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}