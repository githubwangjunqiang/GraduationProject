package com.app.gp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gp.App;
import com.app.gp.MainActivity;
import com.app.gp.Presenter;
import com.app.gp.R;
import com.app.gp.entity.CartData;
import com.app.gp.utils.DecimalCalculate;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author wangjunqiang 2019/4/20 2:18
 */
public class DialogStop extends Dialog {


    private CartData mCartData;
    private Callback mCallback;

    private TextView mTextViewTitle, mTextViewId, mTextViewDuration, mTextViewStopTime,
            mTextViewPrice, mTextViewBtn, mTextViewStartTime;

    public DialogStop(Context context, CartData mCartData, Callback callback) {
        super(context, R.style.user_Dialog_login);
        setCanceledOnTouchOutside(false);
        this.mCartData = mCartData;
        mCallback = callback;
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        if (attributes != null) {
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            attributes.gravity = Gravity.CENTER;
            getWindow().setAttributes(attributes);
        }
        getWindow().setWindowAnimations(R.style.user_Dialog_login);

        if (mCartData == null) {
            dismiss();
            return;
        }
        setData();
    }

    /**
     * 设置数据 计算数据
     */
    private void setData() {
        mTextViewTitle.setText("车辆：" + mCartData.getName());
        mTextViewId.setText("车位编号：" + mCartData.getId());
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mCartData.getStartTime());
        mTextViewStartTime.setText("驶入时间：" + start);
        long millis = System.currentTimeMillis();
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(millis);
        mCartData.setStopTime(millis);
        mTextViewStopTime.setText("离开时间：" + format);

        long timeOld = millis - mCartData.getStartTime();
        double divTime = DecimalCalculate.div(timeOld, 1000D, 3);
        divTime = DecimalCalculate.div(divTime, 60D, 3);

        mTextViewDuration.setText("停车时长：" + divTime + "分钟");
        if (timeOld < 1000) {
            mCartData.setPrice(0);
            mTextViewPrice.setText("停车金额：0元");
            return;
        }
        double hour = DecimalCalculate.div(divTime, 60D, 3);
        long price_cart = MainActivity.price_cart;
        double mul = DecimalCalculate.mul(price_cart, hour);
        mCartData.setPrice(mul);
        mTextViewPrice.setText("停车金额：" + mul + "元");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_stop);
        init();
        setListener();
    }

    /**
     * 初始化
     */
    private void init() {
        mTextViewId = findViewById(R.id.tv_d_bianhao);
        mTextViewTitle = findViewById(R.id.dialog_tvtitle);
        mTextViewStopTime = findViewById(R.id.tv_d_btime);
        mTextViewDuration = findViewById(R.id.tv_d_bdura);
        mTextViewPrice = findViewById(R.id.tv_d_bprice);
        mTextViewBtn = findViewById(R.id.dia_tvbtn);
        mTextViewStartTime = findViewById(R.id.tv_d_btimest);
    }

    /**
     * 监听器
     */
    private void setListener() {
        mTextViewBtn.setOnClickListener(v -> {
            mCartData.setSuccess(true);
            if (mCallback != null) {
                mCallback.onClickOk(mCartData);
            }
            dismiss();
        });
    }


    /**
     * 普通提示框
     */
    public interface Callback {
        /**
         * 点击确定
         *
         * @param mCartData 车辆
         */
        void onClickOk(CartData mCartData);

    }


}
