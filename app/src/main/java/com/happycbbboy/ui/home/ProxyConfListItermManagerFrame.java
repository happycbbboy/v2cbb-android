package com.happycbbboy.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.happycbbboy.R;
import com.happycbbboy.databases.AppDatabase;
import com.happycbbboy.databases.dao.ProxyConfigDao;
import com.happycbbboy.databinding.ProxyConfigItermManagerBinding;
import com.happycbbboy.domain.EventBusMsg;
import com.happycbbboy.domain.ProxyConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProxyConfListItermManagerFrame extends Fragment {

    private ProxyConfigItermManagerBinding binding;
    private ProxyConfigDao proxyConfigDao = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private Integer Id;
    private EditText name;
    private EditText proxyConfigJson;

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
        if (Id != null) {
            mDisposable.add(proxyConfigDao.findById(Id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(proxyConf -> {
                                proxyConfigJson.setText(proxyConf.getProxyConf());
                                name.setText(proxyConf.getName());
                            },
                            throwable -> Log.e("ProxyConfigManagerFragment", "Unable to get username:", throwable)));
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FragmentButtonClicked(EventBusMsg event) {
        switch (event) {
            case PROXY_SETTING_SUBMIT:
                ProxyConfig proxyConfig = new ProxyConfig();
                proxyConfig.setId(Id);
                proxyConfig.setName(name.getText().toString());
                proxyConfig.setProxyConf(proxyConfigJson.getText().toString());

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