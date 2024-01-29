package com.happycbbboy.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.happycbbboy.R;
import com.happycbbboy.databases.AppDatabase;
import com.happycbbboy.databases.dao.ProxyConfigDao;
import com.happycbbboy.databinding.ProxyConfigItermListBinding;
import com.happycbbboy.domain.VPNOptionsImp;
import com.happycbbboy.vpn_lib.VPNInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProxyConfListFrame extends Fragment {
    // 当前点击的对象
    private Integer currentId;

    private ProxyConfigDao proxyConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    //    List<ProxyConfig> proxyConfigArrayList = new ArrayList<>();
    private ProxyConfigItermListBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ProxyConfigItermListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AppDatabase instance = AppDatabase.getInstance(getActivity().getApplication());
        proxyConfigDao = instance.proxyConfigDao();

        ListView proxyConfList = binding.proxyConfList;

        mDisposable.add(proxyConfigDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                            ProxyConfigListViewAdapter proxyConfigListViewAdapter = new ProxyConfigListViewAdapter(getActivity().getApplication(), list);
                            proxyConfList.setAdapter(proxyConfigListViewAdapter);
                            proxyConfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int arg2, long arg3) {
                                    currentId = list.get(arg2).getId();
                                    proxyConfList.setItemChecked(arg2, true);
//                                    proxyConfigListViewAdapter.getItem(arg2)
                                    // Reset the background color of all items
                                    for (int i = 0; i < parent.getChildCount(); i++) {
                                        View listItem = parent.getChildAt(i);
                                        if (listItem instanceof CardView) {
                                            ((CardView) listItem).setCardBackgroundColor(getResources().getColor(R.color.card_default_color,null));
                                        }
                                    }

                                    // Highlight the selected item
                                    if (view instanceof CardView) {
                                        ((CardView) view).setCardBackgroundColor(getResources().getColor(R.color.md_teal_500,null)); // Set your desired highlight color
                                    }
                                }

                            });
                        },
                        throwable -> Log.e("onViewCreated", "proxyConfigDao get all:", throwable)));
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentId == null) {
                    Log.e("START_VPN", "未选择代理配置");
                    Toast.makeText(requireContext(), "代理配置为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDisposable.add(proxyConfigDao.findById(currentId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(proxyConf -> {
                                    if (proxyConf == null) {
                                        Log.e("START_VPN", "未选择代理配置");
                                        Toast.makeText(requireContext(), "代理配置为空", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    VPNOptionsImp vpnOptionsImp = new VPNOptionsImp();
                                    vpnOptionsImp.setProxyConf(proxyConf.getProxyConf());
//                                    vpnOptionsImp.setRoute(proxyConf.getRoute());
//                                    vpnOptionsImp.setExcludePackage(proxyConf.getExcludePackage());
//                                    vpnOptionsImp.setIncludePackage(proxyConf.getIncludePackage());
                                    VPNInterface.Start(getActivity().getApplication(), vpnOptionsImp);
//                                    ((FloatingActionButton) binding.fab).setImageDrawable(AppCompatResources.getDrawable(requireContext(), android.R.drawable.ic_media_pause));
                                },
                                throwable -> {
                                    Log.e("onViewCreated", "proxyConfigDao get all:", throwable);
                                }));

//                Toast.makeText(requireContext(), "vpn start successful!", Toast.LENGTH_LONG).show();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", view1 -> {
//                        }).show();

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}