package com.happycbbboy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.happycbbboy.common.NotifyManager;
import com.happycbbboy.fragment.CenteredTextFragment;
import com.happycbbboy.fragment.ProxyConfigFragment;
import com.happycbbboy.userToolBar.menu.DrawerAdapter;
import com.happycbbboy.userToolBar.menu.DrawerItem;
import com.happycbbboy.userToolBar.menu.SimpleItem;
import com.happycbbboy.userToolBar.menu.SpaceItem;
import com.happycbbboy.vpn_lib.domain.Constants;
import com.happycbbboy.vpn_lib.manager.Notify;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private Logger log = Logger.getLogger("MainActivity");
//
//    // 所有页面配置
//    private Fragment[] fragmentList;


    private static final int SELECT_PROXY_CONF = 0;
    private static final int PROXY_CONF = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_CART = 3;
    private static final int POS_LOGOUT = 5;

    // 左菜单栏名字
    private String[] screenTitles;
    // 左菜单栏icon
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    private BroadcastReceiver vpnSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceive1111111");
            ;
            Notify vpnParam = (Notify) intent.getExtras().get(Notify.PARAM_KEY);
            if (vpnParam != null) {
                showCustomDialog(vpnParam.getTitle(), vpnParam.getMsg());
                if (!Objects.equals(vpnParam.getCode(), Notify.SUCCESS)) {
                    Log.e("VPN_SERVICE", vpnParam.getError());
                }
            }
        }
    };

    private void showCustomDialog(String title, String msg) {
        NotifyManager.showAlertDialog(this, title, msg, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        }, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter(Notify.VPN_CONNECT_ACTION);
        registerReceiver(vpnSuccessReceiver, filter);

//        new ExceptionIntercepter(getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this).withToolbarMenuToggle(toolbar).withMenuOpened(false).withContentClickableWhenMenuOpened(false).withSavedState(savedInstanceState).withMenuLayout(R.layout.menu_left_drawer).inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
//        fragmentList = new Fragment[POS_LOGOUT];

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(createItemFor(SELECT_PROXY_CONF).setChecked(true), createItemFor(PROXY_CONF), createItemFor(POS_MESSAGES), createItemFor(POS_CART), new SpaceItem(48), createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.toolbarList);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(SELECT_PROXY_CONF);
    }

    @Override
    public void onItemSelected(int position) {
        Fragment selectedScreen = null;
        switch (position) {
            case POS_LOGOUT:
                finish();
                break;
            case SELECT_PROXY_CONF:

                selectedScreen = new ProxyConfigFragment(getApplication(), this, null);
//                selectedScreen = new ProxyConfigManagerFragment(getApplication());
                break;
//            case SELECT_PROXY_CONF:
//                selectedScreen = new ProxyConfigManagerFragment(getApplication());
//                break;
            case PROXY_CONF:
                selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
                break;
            case POS_MESSAGES:
                selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
                break;
            case POS_CART:
                selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
                break;
            default:
                log.info("onItemSelected positions:" + position);
                return;
        }
        slidingRootNav.closeMenu();
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position]).withIconTint(color(R.color.textColorSecondary)).withTextTint(color(R.color.textColorPrimary)).withSelectedIconTint(color(R.color.colorAccent)).withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}