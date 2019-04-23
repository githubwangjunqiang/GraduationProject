package com.app.gp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.gp.utils.RxCompose;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author Android-小强 on 2019/04/20
 * @email: 15075818555@163.com
 * @ProjectName: 车位
 */
public class StartActivity extends AppCompatActivity {

    /**
     * 延时器 延时启动主页
     */
    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        subscribe = Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(RxCompose.composeThread())
                .subscribe(aLong -> {
                    openMainActivity();
                }, throwable -> {
                    throwable.printStackTrace();
                    finish();
                });
    }

    /**
     * 启动主页
     */
    private void openMainActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.main_in, R.anim.start_out);
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
        super.onDestroy();
    }
}
