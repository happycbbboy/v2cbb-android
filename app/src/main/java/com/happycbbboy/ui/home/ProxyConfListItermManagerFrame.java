package com.happycbbboy.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.happycbbboy.R;
import com.happycbbboy.databases.AppDatabase;
import com.happycbbboy.databases.RouteConfigDatabase;
import com.happycbbboy.databases.dao.ProxyConfigDao;
import com.happycbbboy.databases.dao.RouteConfigDao;
import com.happycbbboy.databinding.ProxyConfigItermManagerBinding;
import com.happycbbboy.domain.EventBusMsg;
import com.happycbbboy.domain.ProxyConfig;
import com.happycbbboy.domain.RouteConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProxyConfListItermManagerFrame extends Fragment {

    private ProxyConfigItermManagerBinding binding;
    private ProxyConfigDao proxyConfigDao = null;
    private RouteConfigDao routeConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private Integer Id;
    private EditText name;
    private EditText proxyConfigJson;
    private Spinner proxyRouteSelector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ProxyConfigItermManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        proxyConfigDao = AppDatabase.getInstance(getContext()).proxyConfigDao();

        routeConfigDao = RouteConfigDatabase.getInstance(getContext()).proxyConfigDao();

        Bundle arguments = this.getArguments();

        if (arguments != null) {
            Object id = arguments.get("id");
            if (id != null) {
                Id = (int) id;
            }
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = (EditText) getView().findViewById(R.id.ProxyConfigName);
        proxyConfigJson = (EditText) getView().findViewById(R.id.ProxyConfigJson);
        proxyRouteSelector = getView().findViewById(R.id.proxy_route_config_selector);
        Context context = requireContext();

        if (Id != null) {
            mDisposable.add(proxyConfigDao.findById(Id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(proxyConf -> {
                                proxyConfigJson.setText(proxyConf.getProxyConf());
                                name.setText(proxyConf.getName());
                                mDisposable.add(routeConfigDao.getAll()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(allSynchronization -> {
                                            if (allSynchronization != null) {
                                                int onSelectPosion = -1;
                                                Integer id = proxyConf.getRouteProxyId();
                                                for (int i = 0; i < allSynchronization.size(); i++) {
                                                    if (allSynchronization.get(i).getId().equals(id)) {
                                                        onSelectPosion = i;
                                                        break;
                                                    }
                                                }
                                                // 创建适配器，并设置数据
                                                ArrayAdapter<RouteConfig> adapter = new ArrayAdapter<>(context, R.layout.selector_iterm, allSynchronization);

                                                adapter.setDropDownViewResource(R.layout.selector_iterm);

                                                // 设置适配器到Spinner
                                                proxyRouteSelector.setAdapter(adapter);
                                                proxyRouteSelector.setSelection(onSelectPosion);
                                            }
                                        }, throwable -> Log.e("ProxyConfigManagerFragment", "Unable to get username:", throwable)));
                            },
                            throwable -> Log.e("ProxyConfigManagerFragment", "Unable to get username:", throwable)));

        }else {
            mDisposable.add(routeConfigDao.getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(allSynchronization -> {
                        if (allSynchronization != null) {
                            // 创建适配器，并设置数据
                            ArrayAdapter<RouteConfig> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, allSynchronization);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            proxyRouteSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                }
                            });
                            // 设置适配器到Spinner
                            proxyRouteSelector.setAdapter(adapter);
                        }
                    }, throwable -> Log.e("ProxyConfigManagerFragment", "Unable to get username:", throwable)));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mDisposable.dispose();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FragmentButtonClicked(EventBusMsg event) {
        switch (event) {
            case PROXY_SETTING_SUBMIT:
                ProxyConfig proxyConfig = new ProxyConfig();
                proxyConfig.setId(Id);
                proxyConfig.setName(name.getText().toString());
                proxyConfig.setProxyConf(proxyConfigJson.getText().toString());

                SpinnerAdapter adapter = proxyRouteSelector.getAdapter();
                RouteConfig item = (RouteConfig) adapter.getItem(proxyRouteSelector.getFirstVisiblePosition());
                proxyConfig.setRouteProxyId(item.getId());
                mDisposable.add(proxyConfigDao.insertAll(proxyConfig)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                                    navController.popBackStack();
                                    Toast.makeText(requireContext(), "save successful", Toast.LENGTH_SHORT).show();
                                },
                                throwable -> Log.e("ProxyConfigManagerOnClick", "Unable to update username", throwable)));
                break;
            default:
                break;
        }

    }
}