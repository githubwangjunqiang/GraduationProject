package com.app.gp.utils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Android-小强 on 2019/04/20
 * @email: 15075818555@163.com
 * @ProjectName: 车位
 */
public class RxCompose {


    /**
     * 只改变线程调度
     *
     * @return
     */
    public static <T> ObservableTransformer<T, T> composeThread() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

}
