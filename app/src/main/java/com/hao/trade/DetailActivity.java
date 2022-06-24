package com.hao.trade;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class DetailActivity extends AppCompatActivity {
    private TextView mName;
    private TextView mDescription;
    private TextView mPrice;
    private TextView mTime;
    private ImageView mImage;
    private String goodsObjectId;
    private LCObject avObject;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.detail));

        mName = (TextView) findViewById(R.id.name_detail);
        mDescription = (TextView) findViewById(R.id.description_detail);
        mImage = (ImageView) findViewById(R.id.image_detail);
        mTime = findViewById(R.id.tv_time_detail);
        mPrice = findViewById(R.id.tv_price);

        goodsObjectId = getIntent().getStringExtra("goodsObjectId");
        avObject = LCObject.createWithoutData("Product", goodsObjectId);
        avObject.fetchInBackground("owner").subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(LCObject avObject) {
                LCUser owner= avObject.getLCObject("owner");
                if(LCUser.getCurrentUser()!=null && LCUser.getCurrentUser().getUsername().equals(owner.getUsername())){
                    findViewById(R.id.btn_comment).setVisibility(View.GONE);
                    findViewById(R.id.btn_buy).setVisibility(View.GONE);
                    LinearLayout linearLayout = findViewById(R.id.ll_bottom);
                    Button button =  new Button(DetailActivity.this);
                    button.setBackgroundColor(Color.TRANSPARENT);
                    button.setTextColor(Color.RED);
                    button.setText("删除");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                            builder.setMessage("确认删除？")
                                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // START THE GAME!
                                            LCObject todo = LCObject.createWithoutData("Product", goodsObjectId);
                                            todo.deleteInBackground().subscribe(new Observer<LCNull>() {
                                                @Override
                                                public void onSubscribe(@NonNull Disposable d) {}

                                                @Override
                                                public void onNext(LCNull response) {
                                                    // succeed to delete a todo.
                                                }

                                                @Override
                                                public void onError(@NonNull Throwable e) {
                                                    System.out.println("failed to delete a todo: " + e.getMessage());
                                                }

                                                @Override
                                                public void onComplete() {
                                                    Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    }).show();

                        }
                    });
                    linearLayout.addView(button);
                }else{
                    findViewById(R.id.btn_comment).setEnabled(true);
                }
                mName.setText(avObject.getLCObject("owner").getString("username"));
                mDescription.setText(avObject.getString("description"));
                boolean isSold = (boolean) avObject.get("isSold");
                if(isSold){
                    findViewById(R.id.btn_comment).setVisibility(View.GONE);
                    findViewById(R.id.btn_buy).setVisibility(View.GONE);
                }
                mPrice.setText(isSold ? "已卖出" : "￥ " + avObject.get("price"));

                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
                mTime.setText(format.format(avObject.getCreatedAt()));

                Glide.with(DetailActivity.this).load(avObject.get("imageUrl") == null ? null : avObject.get("imageUrl")).into(mImage);
            }
            @Override
            public void onError(Throwable e) {
                Log.e("DetailActivity", e.getMessage());
            }
            @Override
            public void onComplete() {
                avObject.increment("view",1);
                avObject.saveInBackground().subscribe();
            }
        });

        initListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListener(){
        Button btn_buy = findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("下单确认")
                        .setMessage("支付：￥"+avObject.get("price").toString())
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(DetailActivity.this, "订单已取消", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setPositiveButton("立即支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pay(avObject.getInt("price"));
                    }
                }).show();
            }
        });
        Button btn_comment = findViewById(R.id.btn_comment);
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LCObject todo = LCObject.createWithoutData("Product", goodsObjectId);
                todo.increment("want",1);
                todo.saveInBackground().subscribe();

                String phone;
                if (avObject.getString("phone")!=null){
                    phone = avObject.getString("phone").toString();
                    Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));//跳转到拨号界面，同时传递电话号码
                    startActivity(dialIntent);
                }
            }
        });
    }
    private void pay(int count){
        LCQuery<LCUser> query = new LCQuery<>("_User");
        query.getInBackground(LCUser.getCurrentUser().getObjectId()).subscribe(new Observer<LCUser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LCUser lcUser) {
                if(lcUser.getInt("money") >= count){
                    LCObject todo = LCObject.createWithoutData("_User", LCUser.getCurrentUser().getObjectId());
                    todo.increment("money", -count);
                    todo.saveInBackground().subscribe();

                    LCObject product = LCObject.createWithoutData("Product", goodsObjectId);
                    product.put("isSold", true);
                    product.saveInBackground().subscribe();

                    Toast.makeText(DetailActivity.this,"下单成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("com.hao.trade.updateData");
                    intent.putExtra("paid","updateData");
                    sendBroadcast(intent);

                }else{
                    Toast.makeText(DetailActivity.this,"余额不足", Toast.LENGTH_LONG).show();
                }
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