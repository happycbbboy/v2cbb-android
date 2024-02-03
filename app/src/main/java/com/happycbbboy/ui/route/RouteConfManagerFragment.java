package com.happycbbboy.ui.route;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.R;
import com.happycbbboy.databases.RouteConfigDatabase;
import com.happycbbboy.databases.dao.RouteConfigDao;
import com.happycbbboy.databinding.FragmentRouteConfManagerBinding;
import com.happycbbboy.domain.EventBusMsg;
import com.happycbbboy.domain.RouteConfig;
import com.happycbbboy.ui.base.AppItermAdapter;
import com.happycbbboy.ui.base.StringItermAdapter;
import com.happycbbboy.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class RouteConfManagerFragment extends Fragment {

    private Integer id;

    private RouteConfigDao routeConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    FragmentRouteConfManagerBinding binding;
    private EditText name;
    private RecyclerView routeConfigManagerItems;
    private ImageButton addRouteItermConf;
    private RecyclerView appListOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RouteConfigDatabase instance = RouteConfigDatabase.getInstance(getActivity().getApplication());
        routeConfigDao = instance.proxyConfigDao();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentRouteConfManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle arguments = this.getArguments();

        if (arguments != null) {
            Object id = arguments.get("id");
            if (id != null) {
                this.id = (int) id;
            }
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("RouteConfManagerFragment", "onViewCreated accept");
        name = (EditText) getView().findViewById(R.id.route_config_name);
        routeConfigManagerItems = (RecyclerView) getView().findViewById(R.id.route_config_manager_iterms);
        addRouteItermConf = (ImageButton) getView().findViewById(R.id.add_route_iterm_conf);
        appListOptions = (RecyclerView) getView().findViewById(R.id.app_list_options);

        addRouteItermConf.setOnClickListener(v -> {
            RecyclerView.Adapter routeConfigAdapter = routeConfigManagerItems.getAdapter();
            if (routeConfigAdapter != null) {
                StringItermAdapter stringItermAdapter = (StringItermAdapter) routeConfigAdapter;
                List<String> iterms = stringItermAdapter.getIterms();
                iterms.add(0, "");
            } else {
                routeConfigAdapter = new StringItermAdapter(new ArrayList<>(1));
            }

            routeConfigManagerItems.setAdapter(routeConfigAdapter);
        });
        Log.i("RouteConfManagerFragment", "routeConfigManagerItems end accept");

        Log.i("RouteConfManagerFragment", "start get app info");

        if (id != null) {
            mDisposable.add(routeConfigDao.findById(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(routeConfig -> {
                                name.setText(routeConfig.getName());
                                StringItermAdapter stringItermAdapter = new StringItermAdapter(routeConfig.getRoute());
                                routeConfigManagerItems.setAdapter(stringItermAdapter);
                                List<AppUtils.AppInfo> allInstalledApps = AppUtils.getAllInstalledApps(getContext(),routeConfig.getIncludePackage(),routeConfig.getExcludePackage());
                                appListOptions.setAdapter(new AppItermAdapter(allInstalledApps));
                            },
                            throwable -> Log.e("onViewCreated", "proxyConfigDao get all:", throwable)));
        } else {
            List<AppUtils.AppInfo> allInstalledApps = AppUtils.getAllInstalledApps(requireContext(), new ArrayList<>(0), new ArrayList<>(0));
            appListOptions.setAdapter(new AppItermAdapter(allInstalledApps));
        }

        Log.i("RouteConfManagerFragment", "start onViewCreated end");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
// EventBus.getDefault().post(EventBusMsg.ROUTE_SETTING_SUBMIT);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FragmentButtonClicked(EventBusMsg event) {
        switch (event) {
            case ROUTE_SETTING_SUBMIT:
                RouteConfig routeConfig = new RouteConfig();
                routeConfig.setId(id);
                routeConfig.setName(name.getText().toString());
                RecyclerView.LayoutManager layoutManager = routeConfigManagerItems.getLayoutManager();
                ArrayList<String> routes = new ArrayList<>();
                // 检查LayoutManager的类型，以确保它是线性布局管理器（或您使用的特定类型的布局管理器）
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

                    // 获取第一个可见的item的位置和最后一个可见的item的位置
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                    // 遍历可见的item范围
                    for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
                        // 获取对应位置的ViewHolder
                        RecyclerView.ViewHolder viewHolder = routeConfigManagerItems.findViewHolderForAdapterPosition(i);

                        // 检查ViewHolder是否为您自定义的ViewHolder类型
                        if (viewHolder instanceof StringItermAdapter.ViewHolder) {
                            StringItermAdapter.ViewHolder vh = (StringItermAdapter.ViewHolder) viewHolder;

                            // 获取ViewHolder中的TextView（或其他视图）并获取其文本内容
                            EditText viewById = (EditText) vh.itemView.findViewById(R.id.string_iterm_title);
                            routes.add(viewById.getText().toString());
                        }
                    }
                }
                routeConfig.setRoute(routes);
                AppItermAdapter adapter = (AppItermAdapter) appListOptions.getAdapter();
                List<AppUtils.AppInfo> allApps = adapter.getAllApps();
                ArrayList<String> tunnelList = new ArrayList<>();
                ArrayList<String> freeList = new ArrayList<>();
                for (AppUtils.AppInfo allApp : allApps) {
                    if (allApp.getPolicy() == AppUtils.AppInfo.FREEE) {
                        freeList.add(allApp.getPackageName());
                    } else if (allApp.getPolicy() == AppUtils.AppInfo.TUNNEL) {
                        tunnelList.add(allApp.getPackageName());
                    }
                }
                routeConfig.setIncludePackage(tunnelList);
                routeConfig.setExcludePackage(freeList);

                mDisposable.add(routeConfigDao.insertAll(routeConfig)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                                    navController.popBackStack();
                                    Toast.makeText(requireContext(), "route config save successful", Toast.LENGTH_SHORT).show();
                                },
                                throwable -> Log.e("ProxyConfigManagerOnClick", "Unable to update username", throwable)));
                break;
            default:
                break;
        }
    }
}