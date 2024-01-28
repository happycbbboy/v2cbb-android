package com.happycbbboy.ui.home;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.happycbbboy.R;
import com.happycbbboy.databases.AppDatabase;
import com.happycbbboy.databases.dao.ProxyConfigDao;
import com.happycbbboy.databinding.ProxyConfigItermListBinding;
import com.happycbbboy.domain.EventBusConstant;
import com.happycbbboy.domain.EventBusMsg;
import com.happycbbboy.domain.ProxyConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class ProxyConfigListViewAdapter extends BaseAdapter {
    private Application application;
    private LayoutInflater mInflater;
    List<ProxyConfig> proxyConfigArrayList = new ArrayList<>();
    // dao
    private ProxyConfigDao proxyConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private ProxyConfigItermListBinding binding;


    public ProxyConfigListViewAdapter(Application application, List<ProxyConfig> proxyConfigArrayList) {
        this.mInflater = LayoutInflater.from(application);
        this.proxyConfigArrayList = proxyConfigArrayList;
        AppDatabase instance = AppDatabase.getInstance(application);
        proxyConfigDao = instance.proxyConfigDao();
        this.application = application;
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
            view = mInflater.inflate(R.layout.proxy_config_iterm_view, null);

            itermView = new ItermView();
            itermView.title = (TextView) view.findViewById(R.id.ProxyItermTitle);
            itermView.edit = (Button) view.findViewById(R.id.ProxyItermEdit);
            itermView.delete = (Button) view.findViewById(R.id.ProxyItermDelete);

            view.setTag(itermView);
        } else {
            itermView = (ItermView) view.getTag();
        }

        itermView.title.setText(proxyConfig.getName());
        itermView.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusConstant(EventBusMsg.OPEN_PROXY_CONFIG,proxyConfig.id));
            }
        });
        itermView.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisposable.add(proxyConfigDao.delete(proxyConfig)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    Toast.makeText(application, "delete successful", Toast.LENGTH_SHORT).show();
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
