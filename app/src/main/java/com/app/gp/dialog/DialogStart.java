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
import com.app.gp.Presenter;
import com.app.gp.R;

import java.util.List;

/**
 * @author wangjunqiang 2019/4/20 2:18
 */
public class DialogStart extends Dialog {


    private Callback mCallback;

    private TextView mTextViewTitle, mTextViewBtn;
    private EditText mEditText, mEditTextId;

    /**
     * 普通提示框 构造器
     *
     * @param context
     * @param contentMsg
     * @param callback
     */
    public DialogStart(Context context,  Callback callback) {
        super(context, R.style.user_Dialog_login);
        setCanceledOnTouchOutside(true);
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
        setContentView(R.layout.dialog_permission);
        init();
        setListener();
    }

    /**
     * 初始化
     */
    private void init() {
        mTextViewBtn = findViewById(R.id.dia_tvbtn);
        mTextViewTitle = findViewById(R.id.dialog_tvtitle);
        mEditText = findViewById(R.id.dia_et);
        mEditTextId = findViewById(R.id.dia_etid);
    }

    /**
     * 监听器
     */
    private void setListener() {
        mTextViewBtn.setOnClickListener(v -> {
            String name = mEditText.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = mEditTextId.getText().toString().trim();
            if (!TextUtils.isEmpty(id)) {
                List<String> sListDataIds = Presenter.sListDataIds;

                if (!sListDataIds.contains(id)) {
                    Toast.makeText(App.sContext, "没有此编号车位，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
            if (mCallback != null) {
                mCallback.onClickOk(name, id);
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
         * @param name 车辆号牌
         * @param id   车位编号
         */
        void onClickOk(String name, String id);

    }


}
