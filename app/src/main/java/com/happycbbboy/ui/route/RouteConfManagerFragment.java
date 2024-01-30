package com.happycbbboy.ui.route;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.happycbbboy.R;
import com.happycbbboy.databases.RouteConfigDatabase;
import com.happycbbboy.databases.dao.RouteConfigDao;
import com.happycbbboy.databinding.FragmentRouteConfManagerBinding;
import com.happycbbboy.domain.EventBusMsg;
import com.happycbbboy.domain.RouteConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class RouteConfManagerFragment extends Fragment {

    private Integer id;

    private RouteConfigDao routeConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    FragmentRouteConfManagerBinding binding;
    private EditText name;
    private ListView routeConfigManagerIterms;

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
        name = (EditText) getView().findViewById(R.id.route_config_name);
        routeConfigManagerIterms = (ListView) getView().findViewById(R.id.route_config_manager_iterms);

        if (id != null) {
            mDisposable.add(routeConfigDao.findById(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(routeConfig -> {
                                name.setText(routeConfig.getName());
//                                List<String> route = routeConfig.getRoute();
//                                route.add("2");
//                                route.add("1");
//                                route.add("3");
//                                ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity().getApplication(), android.R.layout.simple_list_item_1, route);
//                                routeConfigManagerIterms.setAdapter(arrayAdapter);
                            },
                            throwable -> Log.e("onViewCreated", "proxyConfigDao get all:", throwable)));

        }
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

                mDisposable.add(routeConfigDao.insertAll(routeConfig)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                                    navController.popBackStack();
                                    Toast.makeText(requireContext(), "route config,save successful", Toast.LENGTH_SHORT).show();
                                },
                                throwable -> Log.e("ProxyConfigManagerOnClick", "Unable to update username", throwable)));
                break;
            default:
                break;
        }

    }
}