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
import com.happycbbboy.domain.RouteConfig;
import com.happycbbboy.ui.route.placeholder.PlaceholderContent;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRouteConfListBinding.inflate(inflater, container, false);
        RecyclerView root = (RecyclerView)binding.getRoot();

        RouteConfig item = new RouteConfig();
        item.setId(1);
        item.setName("tes");
        PlaceholderContent.addItem(item);
        RouteConfig item2 = new RouteConfig();
        item2.setId(12);
        item2.setName("tes1");
        PlaceholderContent.addItem(item2);

        mDisposable.add(routeConfigDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                            for (RouteConfig routeConfig : list) {
                                PlaceholderContent.addItem(routeConfig);
                            }
                            root.setAdapter(new RouteConfigItermRecyclerViewAdapter(PlaceholderContent.ITEMS));
                        },
                        throwable -> Log.e("onViewCreated", "proxyConfigDao get all:", throwable)));
        return root;
    }
}