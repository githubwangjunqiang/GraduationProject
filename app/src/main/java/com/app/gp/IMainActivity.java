package com.app.gp;

import com.app.gp.entity.CartData;
import com.app.gp.entity.ListData;

import java.util.List;

/**
 * @author wangjunqiang 2019/4/21 1:48
 **/
public interface IMainActivity {
    /**
     * 初始化
     */
    void init();

    /**
     * 创建监听器
     */
    void setListener();
    /**
     * 刷新信息数据
     * @param listData 本地信息
     * @param price   价格/小时
     * @param title  标题
     */
    void refreshData(List<ListData> listData,long price,String title);

    /**
     * 显示 输入框 车辆驶入
     */
    void showDialogStart();

    /**
     * 显示 车辆进入 操作 信息
     * @param msg
     */
    void showStartCart(String msg);


    /**
     * 显示 车辆使出 操作 信息  数据详情
     */
    void showStopCart();

    /**
     * 显示 统计量
     * @param cartCount 车辆数
     * @param price  总价
     */
    void showStatistics(int cartCount,double price);
}
