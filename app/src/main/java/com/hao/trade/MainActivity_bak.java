package com.hao.trade;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.leancloud.LCLogger;
import cn.leancloud.LeanCloud;


public class MainActivity_bak extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);
        LeanCloud.initialize(this, "jmq5lXmygEPorxRjKMc8nH56-gzGzoHsz", "76OVdWiVxyjU5CHlO8ykdXjB", "https://jmq5lxmy.lc-cn-n1-shared.com");

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    BottomNavigationView navView;
    NavController navController;
    private void initView() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        //获取导航控制器
//        navController = NavHostFragment.findNavController(fragment);
//        //创建自定义的Fragment导航器
//        FixFragmentNavigator fragmentNavigator =
//                new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId());
//        //获取导航器提供者
//        NavigatorProvider provider = navController.getNavigatorProvider();
//        //把自定义的Fragment导航器添加进去
//        provider.addNavigator(fragmentNavigator);
//        //手动创建导航图
//        NavGraph navGraph = initNavGraph(provider, fragmentNavigator);
//        //设置导航图
//        navController.setGraph(navGraph);
//        //底部导航设置点击事件
//        navView = findViewById(R.id.nav_view);
//        navView.setOnNavigationItemSelectedListener(item -> {
//            navController.navigate(item.getItemId());
//            return true;
//            });


//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        // 建立fragment容器的控制器，这个容器就是页面的上的fragment容器
//        navController = NavHostFragment.findNavController(fragment);
//        // 设置成自定义的 FixFragmentNavigator
//        NavigatorProvider navigatorProvider = navController.getNavigatorProvider();
//        navigatorProvider.addNavigator(new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId()));
//        navController.setGraph(R.navigation.mobile_navigation);
//
//        // 获取页面上的底部导航栏控件
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // 配置navigation与底部菜单之间的联系
//        // 底部菜单的样式里面的item里面的ID与navigation布局里面指定的ID必须相同，否则会出现绑定失败的情况
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_message, R.id.navigation_user)
//                .build();
//        // 启动
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

    //手动创建导航图，把3个目的地添加进来
    private NavGraph initNavGraph(NavigatorProvider provider, FixFragmentNavigator fragmentNavigator) {
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        //用自定义的导航器来创建目的地
        FragmentNavigator.Destination destination1 = fragmentNavigator.createDestination();
        destination1.setId(R.id.navigation_home);
        destination1.setClassName(ProductFragment.class.getCanonicalName());
        destination1.setLabel(getResources().getString(R.string.title_home));
        navGraph.addDestination(destination1);

        FragmentNavigator.Destination destination2 = fragmentNavigator.createDestination();
        destination2.setId(R.id.navigation_message);
        destination2.setClassName(MessageFragment.class.getCanonicalName());
        destination2.setLabel(getResources().getString(R.string.title_message));
        navGraph.addDestination(destination2);

        FragmentNavigator.Destination destination3 = fragmentNavigator.createDestination();
        destination3.setId(R.id.navigation_user);
        destination3.setClassName(UserFragment.class.getCanonicalName());
        destination3.setLabel(getResources().getString(R.string.title_user));
        navGraph.addDestination(destination3);

        navGraph.setStartDestination(R.id.navigation_home);

        return navGraph;
    }

//    /**
//     * 重写返回键事件
//     * fix: ComponentActivity.onBackPressed -> ... -> NavController.popBackStack()
//     * 自定义导航器后，会引起NavController管理的回退栈出问题
//     */
//    @Override
//    public void onBackPressed() {
//        int currentId = navController.getCurrentDestination().getId();
//        int startId = navController.getGraph().getStartDestination();
//        //如果当前目的地不是HomeFragment，则先回到HomeFragment
//        if (currentId != startId) {
//            navView.setSelectedItemId(startId);
//        } else {
//            finish();
//        }
//    }


}