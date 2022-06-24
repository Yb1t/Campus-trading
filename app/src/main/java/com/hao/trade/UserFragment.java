package com.hao.trade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.leancloud.LCInstallation;
import cn.leancloud.LCPush;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    private BroadcastReceiver receiver;
    private View mView;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d("action",action);
                switch (action){
                    case "com.hao.trade.updateData":{
                        initData(mView);
                        break;
                    }
                }
            }
        };
        getActivity().registerReceiver(receiver, new IntentFilter("com.hao.trade.updateData"));

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:{
                // navigate to settings screen
                Toast.makeText(getActivity(),"test", Toast.LENGTH_LONG).show();
                return true; //返回 true 以指示已处理点击事件
            }
        }
        return super.onOptionsItemSelected(item); //默认情况下，super 实现会返回 false 以允许继续进行菜单处理
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);
        initData(view);
        mView = view;
        return view;
    }

    private void initView(View view){
        Button btn_about = view.findViewById(R.id.btn_about);
        Button btn_logout = view.findViewById(R.id.btn_logout);
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LCUser.logOut();
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null){
                    activity.setFragmentPosition(0);
                }
                Toast.makeText(getActivity(), "已退出登录",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initData(View view){
        if (LCUser.getCurrentUser() == null){
            return;
        }
        LCQuery<LCUser> query = new LCQuery<>("_User");
        query.getInBackground(LCUser.getCurrentUser().getObjectId()).subscribe(new Observer<LCUser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LCUser lcUser) {
                TextView tv_money = view.findViewById(R.id.tv_money);
                TextView tv_user = view.findViewById(R.id.tv_user_name);
                TextView tv_phone = view.findViewById(R.id.tv_phone);
                tv_user.setText(lcUser.get("username").toString());
                tv_phone.setText(lcUser.get("mobilePhoneNumber").toString());
                tv_money.setText("余额：￥"+lcUser.get("money").toString());

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