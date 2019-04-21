package com.app.gp;

import android.app.Application;
import android.content.Context;

/**
 * @author wangjunqiang 2019/4/20
 **/
public class App extends Application {
    /**
     * 全局上下文
     */
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
