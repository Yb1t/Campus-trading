package com.hao.trade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import cn.leancloud.LCInstallation;
import cn.leancloud.LCLogger;
import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import cn.leancloud.LeanCloud;
import cn.leancloud.push.PushService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LeanCloud.setLogLevel(LCLogger.Level.ERROR);

//      TODO  LeanCloud.initialize(this, "", "", "");

//        启动推送服务，同时设置默认打开的 Activity
        PushService.setDefaultPushCallback(this,MainActivity.class);
        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        // 参数依次为：当前的 context、频道名称、回调对象的类(点击通知栏的通知进入的 Activity
        PushService.subscribe(this, "public", MainActivity.class);
//        在保存 Installation 之前调用 PushService.subscribe
//        在应用启动的时候调用一次，保证设备注册到云端
        LCInstallation.getCurrentInstallation().saveInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(LCObject avObject) {
                // 关联 installationId 到用户表等操作。
                String installationId = LCInstallation.getCurrentInstallation().getInstallationId();
                saveInstallationId(installationId);
                System.out.println("保存成功：" + installationId );
            }
            @Override
            public void onError(Throwable e) {
                System.out.println("保存失败，错误信息：" + e.getMessage());
            }
            @Override
            public void onComplete() {
            }
        });

        initView();
        initBottomNavigation();

    }

    private void saveInstallationId(String id){
        if(LCUser.getCurrentUser() != null){
            LCObject user = LCObject.createWithoutData("_User", LCUser.getCurrentUser().getObjectId());
            user.put("installationId", id);;
            user.saveInBackground().subscribe(new Observer<LCObject>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(LCObject lcObject) {
                    Log.e("installationId","已更新installationId:"+id);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    private List<Fragment> fragments;
    int lastIndex;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        fragments = new ArrayList<>();
        fragments.add(new ProductFragment("createdAt"));
        fragments.add(new ProductFragment("view"));
        fragments.add(new UserFragment());
        // 初始化展示MessageFragment
        setFragmentPosition(0);

    }

    public void initBottomNavigation() {
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setFragmentPosition(0);
                        break;
                    case R.id.navigation_message:
                        if (LCUser.getCurrentUser() == null){
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            return false;
                        }
                        setFragmentPosition(1);
                        break;
                    case R.id.navigation_user:
                        if (LCUser.getCurrentUser() == null){
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            return false;
                        }
                        setFragmentPosition(2);
                        break;
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
    }

    public void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = fragments.get(position);
        Fragment lastFragment = fragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.nav_host_fragment, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }


}
