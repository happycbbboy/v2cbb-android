package com.happycbbboy.fragment;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.happycbbboy.R;
import com.happycbbboy.databases.dao.ProxyConfigDao;
import com.happycbbboy.databases.AppDatabase;
import com.happycbbboy.domain.ProxyConfig;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class ProxyConfigManagerFragment extends Fragment {
    //    private int id = 1;
    private Integer id;
    private EditText proxyConfig;
    private EditText name;
    private Application application;
    // dao
    private ProxyConfigDao proxyConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private AppCompatActivity mainView;

    public ProxyConfigManagerFragment(Application application,AppCompatActivity mainView,Integer id) {
        AppDatabase instance = AppDatabase.getInstance(application);
        proxyConfigDao = instance.proxyConfigDao();
        this.mainView = mainView;
        this.application = application;
        this.id = id;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.proxy_config_manager_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = (EditText) getView().findViewById(R.id.ProxyConfigName);
        proxyConfig = (EditText) getView().findViewById(R.id.ProxyConfigJson);
        if (id!=null) {
            mDisposable.add(proxyConfigDao.findById(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(proxyConf -> {
                                proxyConfig.setText(proxyConf.getProxyConf());
                                name.setText(proxyConf.getName());
                            },
                            throwable -> Log.e("ProxyConfigManagerFragment", "Unable to get username:", throwable)));
        }

        // 创建保存按钮
        ImageButton btnSave = new ImageButton(application);
        btnSave.setImageResource(R.drawable.baseline_done_24);

        // 设置保存按钮的点击事件
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setVisibility(View.GONE);
                ProxyConfig pc = new ProxyConfig();

                pc.setId(id);
                pc.setName(name.getText().toString());
                pc.setProxyConf(proxyConfig.getText().toString());

                mDisposable.add(proxyConfigDao.insertAll(pc)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container, new ProxyConfigFragment(application,mainView, null));
                                    fragmentTransaction.commit();
                                },
                                throwable -> Log.e("ProxyConfigManagerOnClick", "Unable to update username", throwable)));
            }
        });

        // 将保存按钮添加到布局中
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        Toolbar layout = mainView.findViewById(R.id.toolbar);
        layout.addView(btnSave, params);
    }
}
