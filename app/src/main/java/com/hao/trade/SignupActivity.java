package com.hao.trade;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SignupActivity extends AppCompatActivity {
    Button btn_sign_up;
    EditText et_username;
    EditText et_password;
    EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_pwd);
        et_phone = findViewById(R.id.et_phone);
        btn_sign_up = findViewById(R.id.btn_sing_up);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp();
            }
        });

    }



    private void singUp(){
        // 创建实例
        LCUser user = new LCUser();

        user.setUsername(et_username.getText().toString());// 等同于 user.put("username", "Tom")
        user.setPassword(et_password.getText().toString());
        user.setMobilePhoneNumber(et_phone.getText().toString());

        /* io.Reactivex.Observer: Android 中的观察者模式，Rxjava中有两个重要的类Observable和Observer,
        函数响应式编程具体表现为一个观察者（Observer）订阅一个可观察对象（Observable）.
        通过创建Observable发射数据流，经过一系列操作符（Operators）加工处理和线程调度器（Scheduler）在不同线程间的转发，
        最后由观察者接受并做出响应的一个过程。*/
        user.signUpInBackground().subscribe(new Observer<LCUser>() {
            //            在订阅observable时回调，可以在这里调用Disposable.dispose取消订阅或者将Disposable对象保存起来以便在后续某个时刻取消订阅
            public void onSubscribe(Disposable disposable) {}
            //            执行后回调，onNext表示的是整个响应链中的一环，在这里处理响应链中的其中一个任务，可以多次调用。
            public void onNext(LCUser user) {
                // 注册成功
                System.out.println("注册成功。objectId：" + user.getObjectId());
                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_LONG).show();
                finish();
            }
            //            执行后或者链中任一环节出现异常时回调，表示任务执行失败。
            public void onError(Throwable throwable) {
                // 注册失败（通常是因为用户名已被使用）
                Toast.makeText(getApplicationContext(),"注册失败："+throwable.getMessage(),Toast.LENGTH_LONG).show();
            }
            //            执行后回调，表示任务已全部完成，可以在这里做收尾工作
            public void onComplete() {}
        });
    }
}