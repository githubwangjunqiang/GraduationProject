package com.app.gp.entity;

import java.util.List;

/**
 * @author wangjunqiang 2019/4/20 2:15
 **/
public class ListData {
    private String id;
    private List<CartData> mData;

    public ListData(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CartData> getData() {
        return mData;
    }

    public void setData(List<CartData> data) {
        mData = data;
    }
}
