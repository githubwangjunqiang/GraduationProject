package com.app.gp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gp.dialog.DialogGetName;
import com.app.gp.dialog.DialogStart;
import com.app.gp.dialog.DialogStop;
import com.app.gp.dialog.DialogTongji;
import com.app.gp.entity.CartData;
import com.app.gp.entity.ListData;

import java.util.List;

/**
 * @author Android-小强 on 2019/04/20
 * @email: 15075818555@163.com
 * @ProjectName: 车位
 */
public class MainActivity extends AppCompatActivity implements IMainActivity {
    /**
     * 6个车位
     */
    private TextView[] mTextViews;
    /**
     * 6个车位
     */
    private TextView[] mTextViewsId;
    /**
     * 车位每分钟 价钱
     */
    private TextView mTextViewPrice;
    /**
     * 标题
     */
    private TextView mTextViewTitle;
    /**
     * 更改标题按钮
     */
    private TextView mTextViewunTitle;
    /**
     * 车位  进入车位
     */
    private TextView mTextViewIn;
    /**
     * 车位  出车位
     */
    private TextView mTextViewOut;
    /**
     * 车位今日统计
     */
    private TextView mTextViewCount;
    /**
     * 设置
     */
    private TextView mTextViewSetting;
    /**
     * 清空 今日车位统计数据以及车位上的数据
     */
    private TextView mTextViewClearData;
    /**
     * 管理器
     */
    private IPresenter mPresenter;
    /**
     * 一小时 多少元
     */
    public static long price_cart = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();

        refreshData(mPresenter.getCartDatas(), mPresenter.getPrice(), mPresenter.getTitle());
    }


    /**
     * 修改价格 弹窗
     */
    private void showDialogPrice() {
        DialogGetName dialogGetName = new DialogGetName(this, name -> {
            mPresenter.setPrice(Long.parseLong(name));
            refreshData(mPresenter.getCartDatas(), mPresenter.getPrice(), mPresenter.getTitle());
        });
        dialogGetName.show();
        dialogGetName.setMsg("请输入价格", "请输入停车价格（元/小时）",true);

    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.clearView();
        }
        mPresenter = null;
        super.onDestroy();
    }


    @Override
    public void init() {
        mPresenter = new Presenter(this);
        mTextViewIn = findViewById(R.id.tv_stats_in);
        mTextViewOut = findViewById(R.id.tv_stat_out);
        mTextViewCount = findViewById(R.id.tv_tongji);
        mTextViewClearData = findViewById(R.id.tv_clear_data);
        mTextViewSetting = findViewById(R.id.tv_seting);
        mTextViewPrice = findViewById(R.id.tv_pricce);
        mTextViewTitle = findViewById(R.id.tv_title);
        mTextViewunTitle = findViewById(R.id.tv_untitle);
        mTextViews = new TextView[]{
                findViewById(R.id.tv_p1),
                findViewById(R.id.tv_p2),
                findViewById(R.id.tv_p3),
                findViewById(R.id.tv_p4),
                findViewById(R.id.tv_p5),
                findViewById(R.id.tv_p6),
        };
        mTextViewsId = new TextView[]{
                findViewById(R.id.tv_p1id),
                findViewById(R.id.tv_p2id),
                findViewById(R.id.tv_p3id),
                findViewById(R.id.tv_p4id),
                findViewById(R.id.tv_p5id),
                findViewById(R.id.tv_p6id),
        };
        for (int i = 0; i < Presenter.sListDataIds.size(); i++) {
            mTextViewsId[i].setText(Presenter.sListDataIds.get(i));
        }
    }

    @Override
    public void setListener() {
        //修改标题
        mTextViewunTitle.setOnClickListener(v -> showDialogTitle());
        //价格弹窗
        mTextViewSetting.setOnClickListener(v -> showDialogPrice());
        //清空数据
        mTextViewClearData.setOnClickListener(v -> mPresenter.clearDatas());
        //进车位
        mTextViewIn.setOnClickListener(v -> showDialogStart());
        //出车位
        mTextViewOut.setOnClickListener(v -> showStopCart());
        //统计
        mTextViewCount.setOnClickListener(v -> mPresenter.getStatistics());
    }

    /**
     * 修改标题 弹框
     */
    private void showDialogTitle() {
        List<ListData> cartDatas = mPresenter.getCartDatas();
        for (int i = 0; i < cartDatas.size(); i++) {
            ListData data = cartDatas.get(i);
            List<CartData> dataData = data.getData();
            if (dataData != null && !dataData.isEmpty()) {
                Toast.makeText(this, "请先清空数据", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        DialogGetName dialogGetName = new DialogGetName(this, name -> {
            mPresenter.setTitle(name);
            refreshData(mPresenter.getCartDatas(), mPresenter.getPrice(), mPresenter.getTitle());
        });
        dialogGetName.show();
        dialogGetName.setMsg("请输入标题", "请输入标题");
    }

    @Override
    public void showDialogStart() {
        if (mPresenter.getHasCart()) {
            new DialogStart(this, (name, id) -> {
                if (!TextUtils.isEmpty(id)) {
                    if (!mPresenter.hasEmptyId(id)) {
                        Toast.makeText(this, "该车位编号已有车辆", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mPresenter.quiryCart(name) !=null) {
                        Toast.makeText(this, "已有车辆号牌", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                CartData data = new CartData(name, System.currentTimeMillis());
                mPresenter.startCart(id, data);
            }).show();
        } else {
            Toast.makeText(this, "车辆已满", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void refreshData(List<ListData> listData, long price, String title) {
        for (int i = 0; i < listData.size(); i++) {
            ListData data = listData.get(i);
            List<CartData> dataCart = data.getData();
            if (dataCart != null && !dataCart.isEmpty()) {
                CartData cartData = dataCart.get(dataCart.size() - 1);
                if (!cartData.isSuccess()) {
                    mTextViews[i].setText(cartData.getName());
                    mTextViews[i].setSelected(true);
                    continue;
                }
            }
            mTextViews[i].setText("空位");
            mTextViews[i].setSelected(false);
        }
        price_cart = price;
        mTextViewPrice.setText(price_cart + "元/小时");
        mTextViewTitle.setText(title);
    }

    @Override
    public void showStartCart(String msg) {
        Toast.makeText(App.sContext, msg, Toast.LENGTH_SHORT).show();
        refreshData(mPresenter.getCartDatas(), mPresenter.getPrice(), mPresenter.getTitle());
    }


    @Override
    public void showStopCart() {
        if (mPresenter.hasCartSize()) {
            new DialogGetName(this, name -> {
                CartData cartData = mPresenter.quiryCart(name);
                if (cartData == null) {
                    Toast.makeText(this, "没有此号牌车辆", Toast.LENGTH_SHORT).show();
                    return;
                }
                new DialogStop(this, cartData, mCartData -> mPresenter.stopCart(mCartData)).show();
            }).show();
        } else {
            Toast.makeText(this, "停车场没有车辆", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showStatistics(int cartCount, double price) {
        new DialogTongji(this, cartCount+"", price+"").show();
    }
}
