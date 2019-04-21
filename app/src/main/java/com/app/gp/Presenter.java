package com.app.gp;

import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.widget.Toast;

import com.app.gp.entity.CartData;
import com.app.gp.entity.ListData;
import com.app.gp.utils.DecimalCalculate;
import com.app.gp.utils.SpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author wangjunqiang 2019/4/20 23:18
 **/
public class Presenter implements IPresenter {

    /**
     * 数据库 key
     */
    private static final String DATA_KEY = "data_key";
    /**
     * 钱 价钱 每分钟 多少分
     */
    private static final String PRICE_TAG = "price_key";
    /**
     * 本地存入 标题 key
     */
    private static final String TITLE_TAG = "title_tag";
    /**
     * 界面的引用
     */
    private Reference<IMainActivity> mIMainActivity;
    /**
     * 车位编号
     */
    public static final List<String> sListDataIds = Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3");


    public Presenter(IMainActivity iMainActivity) {
        mIMainActivity = new WeakReference<>(iMainActivity);
    }

    @Override
    public List<ListData> getCartDatas() {
        List<ListData> listData = null;
        String string = (String) SpUtils.get(App.sContext, DATA_KEY, "");
        if (!TextUtils.isEmpty(string)) {
            try {
                listData = new Gson().fromJson(string, new TypeToken<List<ListData>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        if (listData == null) {
            listData = new ArrayList<>();
        }
        if (listData.isEmpty()) {
            for (int i = 0; i < sListDataIds.size(); i++) {
                ListData dataList = new ListData(sListDataIds.get(i));
                listData.add(dataList);
            }
        }
        return listData;
    }

    @Override
    public boolean saveList(List<ListData> listData) {
        if (listData != null) {
            return SpUtils.putCommit(App.sContext, DATA_KEY, new Gson().toJson(listData));
        }
        Toast.makeText(App.sContext, "存入数据失败", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean getHasCart() {
        List<ListData> list = getCartDatas();

        for (ListData data : list) {
            List<CartData> cart = data.getData();
            if (cart == null || cart.isEmpty()) {
                return true;
            }
            CartData cartData = cart.get(cart.size() - 1);
            if (cartData.isSuccess()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void startCart(String id, CartData cartData) {
        try {
            List<ListData> cartDatas = getCartDatas();
            if (TextUtils.isEmpty(id)) {
                id = getEmtiyId();
            }
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(App.sContext, "获取车位编号失败", Toast.LENGTH_SHORT).show();
                return;
            }
            ListData data = cartDatas.get(sListDataIds.indexOf(id));

            List<CartData> dataList = data.getData();
            if (dataList == null) {
                dataList = new ArrayList<>();
            }
            cartData.setId(id);
            dataList.add(cartData);
            data.setData(dataList);
            saveList(cartDatas);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(App.sContext, "车辆驶入失败", Toast.LENGTH_SHORT).show();
            return;
        }
        mIMainActivity.get().showStartCart("车辆" + cartData.getName() + "驶入" + id + "车位");
    }

    @Override
    public String getEmtiyId() {
        List<ListData> list = getCartDatas();

        for (ListData data : list) {
            List<CartData> cart = data.getData();
            if (cart == null || cart.isEmpty()) {
                return data.getId();
            }
            CartData cartData = cart.get(cart.size() - 1);
            if (cartData.isSuccess()) {
                return data.getId();
            }
        }
        return "";
    }

    @Override
    public CartData quiryCart(String name) {
        List<ListData> cartDatas = getCartDatas();
        for (int i = 0; i < cartDatas.size(); i++) {
            ListData data = cartDatas.get(i);
            List<CartData> dataData = data.getData();
            if (dataData != null && !dataData.isEmpty()) {
                CartData cartData = dataData.get(dataData.size() - 1);
                if (cartData != null && cartData.getName().equals(name) && !cartData.isSuccess()) {
                    return cartData;
                }
            }
        }
        return null;
    }

    @Override
    public void stopCart(CartData cartData) {
        boolean success = false;
        List<ListData> cartDatas = getCartDatas();
        for (int i = 0; i < cartDatas.size(); i++) {
            ListData data = cartDatas.get(i);
            if (cartData.getId().equals(data.getId())) {
                List<CartData> dataData = data.getData();
                if (dataData != null && !dataData.isEmpty()) {
                    CartData cartData_j = dataData.get(dataData.size() - 1);
                    if (cartData_j != null
                            && cartData_j.getName().equals(cartData.getName())
                            && cartData_j.getId().equals(cartData.getId())
                            && !cartData_j.isSuccess()) {
                        dataData.remove(cartData_j);
                        dataData.add(cartData);
                        data.setData(dataData);
                        success = true;
                        break;
                    }
                }
            }
        }
        if (success) {
            saveList(cartDatas);
            mIMainActivity.get().refreshData(getCartDatas(), getPrice(), getTitle());
            Toast.makeText(App.sContext, "车辆已经驶出", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(App.sContext, "结算失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setPrice(long price) {
        SpUtils.putCommit(App.sContext, PRICE_TAG, price);
    }

    @Override
    public long getPrice() {
        return (long) SpUtils.get(App.sContext, PRICE_TAG, 1L);
    }


    @Override
    public void setTitle(String title) {
        SpUtils.putCommit(App.sContext, TITLE_TAG, title);
    }

    @Override
    public String getTitle() {
        return (String) SpUtils.get(App.sContext, TITLE_TAG, "北京市雍和大厦");
    }

    @Override
    public void getStatistics() {

        int count = 0;
        double price = 0;

        long startTimeOfDay = getStartTimeOfDay(null);

        List<ListData> cartDatas = getCartDatas();
        for (int i = 0; i < cartDatas.size(); i++) {
            ListData data = cartDatas.get(i);
            List<CartData> dataData = data.getData();
            if (dataData != null) {
                for (CartData cartData : dataData) {
                    long startTime = cartData.getStartTime();
                    long stopTime = cartData.getStopTime();
                    if(startTime>startTimeOfDay){
                        count++;
                    }
                    if (cartData.isSuccess()&&stopTime>startTimeOfDay) {
                        price =DecimalCalculate.add(price,cartData.getPrice());
                    }
                }
            }
        }
        mIMainActivity.get().showStatistics(count, price);
    }
    //获取当天（按当前传入的时区）00:00:00所对应时刻的long型值
    private long getStartTimeOfDay(String timeZone) {
        String tz = TextUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    @Override
    public void clearDatas() {
        boolean b = SpUtils.clearCommit(App.sContext);
        if (b) {
            Toast.makeText(App.sContext, "清除数据成功", Toast.LENGTH_SHORT).show();
            mIMainActivity.get().refreshData(getCartDatas(), getPrice(), getTitle());
        } else {
            Toast.makeText(App.sContext, "清除数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void clearView() {
        if (mIMainActivity != null) {
            mIMainActivity.clear();
            mIMainActivity = null;
        }
    }

    @Override
    public boolean hasEmptyId(String id) {
        List<ListData> list = getCartDatas();
        if (!sListDataIds.contains(id)) {
            return false;
        }
        ListData cart = list.get(sListDataIds.indexOf(id));

        List<CartData> dataCart = cart.getData();

        if (dataCart == null || dataCart.isEmpty()) {
            return true;
        }
        if (dataCart.get(dataCart.size() - 1).isSuccess()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasCartSize() {
        List<ListData> cartDatas = getCartDatas();
        for (int i = 0; i < cartDatas.size(); i++) {
            ListData data = cartDatas.get(i);
            List<CartData> dataData = data.getData();
            if (dataData != null && !dataData.isEmpty()) {
                for (int j = 0; j < dataData.size(); j++) {
                    if (!dataData.get(j).isSuccess()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
