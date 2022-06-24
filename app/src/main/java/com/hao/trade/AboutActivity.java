package com.hao.trade;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cn.leancloud.LCInstallation;
import cn.leancloud.LCPush;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    private void testPush(){
        LCQuery<LCInstallation> pushQuery = LCInstallation.getQuery();
// 假设 THE_INSTALLATION_ID 是保存在用户表里的 installationId，
// 可以在应用启动的时候获取并保存到用户表
        pushQuery.whereEqualTo("installationId", "a17322c268391c0e2a3e7cfd4228f221");
        LCPush.sendMessageInBackground("Tarara invited you to play Arc Symphony with her!",pushQuery).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Object object) {
                System.out.println("推送成功" + object);
            }
            @Override
            public void onError(Throwable e) {
                System.out.println("推送失败，错误信息：" + e.getMessage());
            }
            @Override
            public void onComplete() {
            }
        });
    }
}