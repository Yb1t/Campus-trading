package com.hao.trade;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    private ListView lvGoodsList;

    private List<LCObject> goodsData;
    private GoodsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final String orderBy;


    public ProductFragment(String orderBy) {
        // Required empty public constructor
        this.orderBy = orderBy;
    }

    public static ProductFragment newInstance(String orderBy) {
        ProductFragment fragment = new ProductFragment(orderBy);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(this.getClass().getName(), "onCreate");

//        setHasOptionsMenu(true)可告知系统您的 Fragment 想要接收与菜单相关的回调。
//        当发生与菜单相关的事件（创建、点击等等）时，先对 Activity 调用事件处理方法，然后再对 Fragment 调用。
        setHasOptionsMenu(true); //向 Activity 注册

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_publish:{
                if(LCUser.getCurrentUser() != null){
                    Intent intent = new Intent(getActivity(), PublishActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item); //默认情况下，super 实现会返回 false 以允许继续进行菜单处理
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(this.getClass().getName(), "onCreateView");

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        goodsData = new ArrayList<>();
        adapter = new GoodsAdapter(getActivity(),
                R.layout.item_list_main, goodsData);
        lvGoodsList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(this.getClass().getName(), "onResume");
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(this.getClass().getName(), "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(this.getClass().getName(),"onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(this.getClass().getName(), "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(this.getClass().getName(), "onDestroyView");
    }

    protected void initData() {
        goodsData.clear();
        LCQuery<LCObject> avQuery = new LCQuery<>("Product");
        avQuery.orderByDescending(orderBy);
        avQuery.include("owner");
        avQuery.findInBackground().subscribe(new Observer<List<LCObject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<LCObject> list) {
                goodsData.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void initView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        lvGoodsList = view.findViewById(R.id.lv_goods_list);
        lvGoodsList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int i, long l) {
                        Intent intent = new Intent(getActivity(),
                                DetailActivity.class);
                        LCObject data = adapter.getItem(i);
//                        intent.putExtra(Constants.NEWS_DETAIL_URL_KEY,
//                                data.get);
                        startActivity(intent);
                    }
                });


    }


}