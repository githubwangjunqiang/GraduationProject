package com.app.gp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gp.utils.SpUtils;

public class LoginActivity extends AppCompatActivity {


    private static final String LOGIN_KEY = "login_key";
    private static final String LOGIN_PASS = "login_pass";
    private TextView mTextViewLogin;
    private TextInputEditText mTextInputEditTextName, mTextInputEditTextPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        setListener();
        setData();
    }

    private void init() {
        mTextInputEditTextName = findViewById(R.id.login_etname);
        mTextInputEditTextPass = findViewById(R.id.login_etpass);
        mTextViewLogin = findViewById(R.id.tvbtnlogin);
    }

    private void setListener() {
        mTextViewLogin.setOnClickListener(v -> {
            String name = mTextInputEditTextName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            String pass = mTextInputEditTextPass.getText().toString().trim();
            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }
            //存入 用户名
            SpUtils.putCommit(this, LOGIN_KEY, name);
            SpUtils.putCommit(this, LOGIN_PASS, pass);

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void setData() {
        String name = (String) SpUtils.get(this, LOGIN_KEY, "");
        if (TextUtils.isEmpty(name)) {
            return;
        }
        mTextInputEditTextName.setText(name);
    }
}
