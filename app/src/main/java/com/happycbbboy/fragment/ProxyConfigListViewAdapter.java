package com.happycbbboy.fragment;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.happycbbboy.R;
import com.happycbbboy.databases.dao.ProxyConfigDao;
import com.happycbbboy.databases.AppDatabase;
import com.happycbbboy.domain.ProxyConfig;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class ProxyConfigListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Application application;
    private FragmentActivity activity;

    List<ProxyConfig> proxyConfigArrayList = new ArrayList<>();
    // dao
    private ProxyConfigDao proxyConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private AppCompatActivity mainView;

    public ProxyConfigListViewAdapter(Application application,AppCompatActivity mainView, FragmentActivity activity, List<ProxyConfig> proxyConfigArrayList) {
        this.application  = application;
        this.activity = activity;
        this.mainView = mainView;


        this.mInflater = LayoutInflater.from(application);
        this.proxyConfigArrayList = proxyConfigArrayList;
        AppDatabase instance = AppDatabase.getInstance(application);
        proxyConfigDao = instance.proxyConfigDao();
    }

    @Override
    public int getCount() {
        return proxyConfigArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return proxyConfigArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ProxyConfig proxyConfig = proxyConfigArrayList.get(i);
        ItermView itermView;
        if (view == null) {
            view = mInflater.inflate(R.layout.proxy_config_iterm, null);

            itermView = new ItermView();
            itermView.title = (TextView) view.findViewById(R.id.ProxyItermTitle);
            itermView.edit = (Button) view.findViewById(R.id.ProxyItermEdit);
            itermView.delete = (Button) view.findViewById(R.id.ProxyItermDelete);

            view.setTag(itermView); // 重要！确保将ViewHolder存储在Tag中
        } else {
            itermView = (ItermView) view.getTag();
        }

        itermView.title.setText(proxyConfig.getName());
        itermView.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new ProxyConfigManagerFragment(application,mainView, proxyConfig.id));
                fragmentTransaction.commit();
            }
        });
        itermView.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisposable.add(proxyConfigDao.delete(proxyConfig)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    androidx.fragment.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container, new ProxyConfigFragment(application, mainView,null));
                                    fragmentTransaction.commit();
                                },
                                throwable -> Log.e("ProxyConfigManagerOnClick", "Unable to update username", throwable)));

            }
        });
        return view;
    }

    static class ItermView {
        public TextView title;
        public Button edit;
        public Button delete;
    }
}
