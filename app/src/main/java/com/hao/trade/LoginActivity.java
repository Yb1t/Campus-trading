package com.hao.trade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {
    EditText et_username;
    EditText et_password;
    Button btn_login;
    Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_pwd);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_sing_up_in_login);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login(){
        LCUser.logIn(et_username.getText().toString(), et_password.getText().toString()).subscribe(new Observer<LCUser>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(LCUser user) {
                // 登录成功
                System.out.println("登录成功。objectId：" + user.getObjectId());
                Intent intent = new Intent("com.hao.trade.updateData");
                intent.putExtra("login","updateData");
                sendBroadcast(intent);

                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
                finish();
            }
            public void onError(Throwable throwable) {
                // 登录失败（可能是密码错误）
                Toast.makeText(getApplicationContext(),"登录失败："+throwable.getMessage(),Toast.LENGTH_LONG).show();
            }
            public void onComplete() {}
        });
    }
}