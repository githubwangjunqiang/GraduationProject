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
import com.app.gp.MainActivity;
import com.app.gp.R;
import com.app.gp.entity.CartData;
import com.app.gp.utils.DecimalCalculate;

import java.text.SimpleDateFormat;

/**
 * @author wangjunqiang 2019/4/20 2:18
 */
public class DialogGetName extends Dialog {


    private Callback mCallback;

    private TextView mTextViewBtn, mTextViewTitle;
    private EditText mEditText;
    /**
     * 是价格  那么只能输入整数
     */
    private boolean price;

    public DialogGetName(Context context, Callback callback) {
        super(context, R.style.user_Dialog_login);
        setCanceledOnTouchOutside(false);
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_getname);
        init();
        setListener();
    }

    /**
     * 初始化
     */
    private void init() {
        mEditText = findViewById(R.id.dia_et);
        mTextViewBtn = findViewById(R.id.dia_tvbtn);
        mTextViewTitle = findViewById(R.id.dialog_tvtitle);
    }

    /**
     * 监听器
     */
    private void setListener() {
        mTextViewBtn.setOnClickListener(v -> {
            String trim = mEditText.getText().toString().trim();
            if (TextUtils.isEmpty(trim)) {
                Toast.makeText(App.sContext, "您没有输入任何内容", Toast.LENGTH_SHORT).show();
                return;
            }
            if (price) {
                try {
                    if(trim.contains(".")){
                        mEditText.setText("");
                        Toast.makeText(getContext(), "输入错误，请输入整数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long price = Long.parseLong(trim);
                    if (price < 0) {
                        mEditText.setText("");
                        Toast.makeText(getContext(), "输入错误，请输入正整数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "输入错误，请输入整数", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (mCallback != null) {
                mCallback.onClickOk(trim);
            }
            dismiss();
        });
    }

    /**
     * 设置显示的数据
     */
    public void setMsg(String title, String hint) {
        mEditText.setHint(hint);
        mTextViewTitle.setText(title);
    }

    /**
     * 设置显示的数据
     */
    public void setMsg(String title, String hint, boolean price) {
        mEditText.setHint(hint);
        mTextViewTitle.setText(title);
        this.price = price;
    }

    /**
     * 普通提示框
     */
    public interface Callback {
        /**
         * 点击确定
         *
         * @param name 车辆号牌
         */
        void onClickOk(String name);

    }


}
