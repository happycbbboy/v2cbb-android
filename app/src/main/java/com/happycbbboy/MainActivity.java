package com.happycbbboy;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.happycbbboy.databinding.ActivityMainBinding;
import com.happycbbboy.domain.EventBusConstant;
import com.happycbbboy.domain.EventBusMsg;
import com.happycbbboy.manager.message.BroadCastReceiverImpl;
import com.happycbbboy.ui.home.ProxyConfListItermManagerFrame;
import com.happycbbboy.utils.AppUtils;
import com.happycbbboy.vpn_lib.manager.Notify;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Menu menu;

/*    private final BroadcastReceiver vpnSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Notify vpnParam = (Notify) intent.getExtras().get(Notify.PARAM_KEY);
            if (vpnParam != null) {
                Toast.makeText(MainActivity.this, vpnParam.getTitle() + ":" + vpnParam.getMsg(), Toast.LENGTH_SHORT).show();
                if (!Objects.equals(vpnParam.getCode(), Notify.SUCCESS)) {
                    Log.e("VPN_SERVICE", vpnParam.getError());
                }
            }
        }
    };*/


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadCastReceiverImpl broadCastReceiver = new BroadCastReceiverImpl(MainActivity.this);
        registerReceiver(broadCastReceiver, new IntentFilter(Notify.VPN_CONNECT_ACTION), Context.RECEIVER_EXPORTED);
        List<AppUtils.AppInfo> allInstalledApps = AppUtils.getAllInstalledApps(this);
        for (AppUtils.AppInfo allInstalledApp : allInstalledApps) {
            System.out.println( allInstalledApp.getAppName()+"------>"+allInstalledApp.getPackageName());
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.proxy_conf_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理选项菜单项的点击事件
        int itemId = item.getItemId();
        if (itemId == R.id.new_proxy_settings) {
            OpenNewProxySettings(null);
            return true;
        } else if (itemId == R.id.new_route_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void OpenNewProxySettings(Integer i) {

        Bundle bundle = new Bundle();
        if (i != null) {
            bundle.putInt("id", i);
        }
        ProxyConfListItermManagerFrame fragment = new ProxyConfListItermManagerFrame();
        fragment.setArguments(bundle);
        // 导航到目标 Fragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle1) -> {
            int currentDestinationId = navDestination.getId();
            // 检查是否是回退按钮触发的
            if (currentDestinationId == R.id.nav_home) {
                menu.clear();
                getMenuInflater().inflate(R.menu.proxy_conf_menu, menu);

            } else if (currentDestinationId == R.id.proxy_config_iterm_manager_fragment){
                // 启动新的Activity
                menu.clear();
                //添加标题栏的确定按钮
                MenuItem submit = menu.add(0, 0, 0, "");
                submit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//主要是这句话
                submit.setOnMenuItemClickListener(menuItem -> {
                    EventBus.getDefault().post(EventBusMsg.SUBMIT);
                    return true;
                });
                submit.setIcon(R.mipmap.submit_config);//设置图标

            }else {
                menu.clear();
                getMenuInflater().inflate(R.menu.proxy_conf_menu, menu);
            }
        });
        navController.navigate(R.id.save_proxy_config_iterm_manager_fragment, bundle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MainActivityListener(EventBusConstant event) {
        EventBusMsg eventBusMsg = event.getEventBusMsg();
        switch (eventBusMsg) {
            case OPEN_PROXY_CONFIG:
                OpenNewProxySettings(event.getId());
            default:
                break;
        }
    }
}
