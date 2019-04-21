package com.app.gp;

import com.app.gp.entity.CartData;
import com.app.gp.entity.ListData;

import java.util.List;

/**
 * @author wangjunqiang 2019/4/20 23:22
 **/
public interface IPresenter {
    /**
     * 查询 车辆 总量 显示
     * @return  返回本地数据
     */
    List<ListData> getCartDatas();

    /**
     * 存入数据
     * @param listData
     * @return
     */
    boolean saveList(List<ListData> listData);

    /**
     * 是否还有 车位
     * @return
     */
    boolean getHasCart();

    /**
     * 随机 获取车位 id
     * @return
     */
    String getEmtiyId();
    /**
     * 车辆进入
     * @param id  车库编号
     * @param cartData 车辆
     */
    void startCart(String id, CartData cartData);

    /**
     * 查询车辆
     * @param name 车辆号牌
     * @return  re
     */
    CartData quiryCart(String name);

    /**
     * 车辆驶出
     * @param cartData 车辆
     */
    void stopCart(CartData cartData);


    /**
     * 设置 每小时 停车费用
     * @param price
     */
    void setPrice(long price);

    /**
     * 获取价格
     * @return
     */
    long getPrice();

    /**
     * 设置 title
     * @param title
     */
    void setTitle(String title);

    /**
     * 获取 title
     * @return
     */
    String getTitle();

    /**
     * 查询统计量 今天车辆数量   今天停车费用
     */
    void getStatistics();

    /**
     * 清空数据
     */
    void clearDatas();

    /**
     * 清空引用
     */
    void clearView();

    /**
     * 此车位 编号 是否可以停车
     * @param id
     * @return
     */
    boolean hasEmptyId(String id);

    /**
     * 库里 有没有 车
     * @return
     */
    boolean hasCartSize();

}
