package com.app.gp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gp.App;
import com.app.gp.R;

/**
 * @author wangjunqiang 2019/4/20 2:18
 */
public class DialogTongji extends Dialog {


    private TextView mTextViewBtn, mTextViewPrice, mTextViewCount;
    private String price, count;

    public DialogTongji(Context context, String count, String price) {
        super(context, R.style.user_Dialog_login);
        setCanceledOnTouchOutside(false);
        this.count = count;
        this.price = price;
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tongji);
        init();
        setListener();
    }

    /**
     * 初始化
     */
    private void init() {
        mTextViewPrice = findViewById(R.id.tv_price);
        mTextViewBtn = findViewById(R.id.dia_tvbtn);
        mTextViewCount = findViewById(R.id.tv_count);

        mTextViewCount.setText("今日停车总数量：" + count + "台");
        mTextViewPrice.setText("今日停车总金额：" + price + "元");
    }

    /**
     * 监听器
     */
    private void setListener() {
        mTextViewBtn.setOnClickListener(v -> {
            dismiss();
        });
    }


}
