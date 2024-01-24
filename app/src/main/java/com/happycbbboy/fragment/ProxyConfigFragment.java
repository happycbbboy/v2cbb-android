package com.happycbbboy.fragment;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.happycbbboy.R;
import com.happycbbboy.databases.dao.ProxyConfigDao;
import com.happycbbboy.databases.AppDatabase;
import com.happycbbboy.domain.ProxyConfig;
import com.happycbbboy.domain.VPNOptionsImp;
import com.happycbbboy.vpn_lib.VPNInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yarolegovich on 25.03.2017.
 */

public class ProxyConfigFragment extends Fragment {
    private Integer currentId;
    private Application application;
    private ListView proxyConfigList;
    private ProxyConfigListViewAdapter proxyConfigListViewAdapter;
    private AppCompatActivity mainView;
    // dao
    private ProxyConfigDao proxyConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public ProxyConfigFragment(Application application, AppCompatActivity mainView, Integer id) {
        this.application = application;
        this.currentId = id;
        this.mainView = mainView;
        proxyConfigDao = AppDatabase.getInstance(application).proxyConfigDao();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.proxy_config_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // 列表展示
        proxyConfigList = (ListView) getView().findViewById(R.id.proxyConfList);

        mDisposable.add(proxyConfigDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                            proxyConfigListViewAdapter = new ProxyConfigListViewAdapter(application, mainView, getActivity(), list);
                            proxyConfigList.setAdapter(proxyConfigListViewAdapter);
                            proxyConfigList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    currentId = arg2;
                                    proxyConfigList.setItemChecked(arg2, true);
                                }
                            });
                        },
                        throwable -> Log.e("onViewCreated", "proxyConfigDao get all:", throwable)));

        // 新增
        FloatingActionButton addProxyIterm = (FloatingActionButton) getView().findViewById(R.id.AddProxyIterm);
        addProxyIterm.setOnClickListener(view1 -> {
            androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ProxyConfigManagerFragment(application, mainView, null));
            fragmentTransaction.commit();
        });

        // 启动vpn
        FloatingActionButton start = (FloatingActionButton) getView().findViewById(R.id.StartProxy);
        // 获取当前选择的 conf
        start.setOnClickListener(view1 -> {
            if (currentId == null) {
                Log.e("START_VPN", "未选择代理配置");
                return;
            }
            ProxyConfig proxyConfig = proxyConfigListViewAdapter.proxyConfigArrayList.get(currentId);
            if (proxyConfig == null) {
                Log.e("START_VPN", "未选择代理配置");
                return;
            }
            mDisposable.add(proxyConfigDao.findById(proxyConfig.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(proxyConf -> {
                                if (proxyConf == null) {
                                    Log.e("START_VPN", "未选择代理配置");
                                    return;
                                }
                                VPNOptionsImp vpnOptionsImp = new VPNOptionsImp();
                                vpnOptionsImp.setProxyConf(proxyConf.getProxyConf());
                                vpnOptionsImp.setRoute(proxyConf.getRoute());
                                vpnOptionsImp.setExcludePackage(proxyConf.getExcludePackage());
                                vpnOptionsImp.setIncludePackage(proxyConf.getIncludePackage());
                                VPNInterface.Start(application, vpnOptionsImp);
                            },
                            throwable -> {
                                Log.e("onViewCreated", "proxyConfigDao get all:", throwable);
                            }));
        });

    }
}
