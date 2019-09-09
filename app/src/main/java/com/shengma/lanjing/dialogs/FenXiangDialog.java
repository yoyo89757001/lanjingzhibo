package com.shengma.lanjing.dialogs;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shengma.lanjing.R;

public class FenXiangDialog extends DialogFragment implements View.OnClickListener {

    private Window window;
    private LinearLayout l1,l2,l3,l4;
    private ImageView guanbi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View  view = inflater.inflate(R.layout.fenxiang_dialog, null);
        guanbi=view.findViewById(R.id.guanbi);
        guanbi.setOnClickListener(this);
        l1=view.findViewById(R.id.l1);
        l2=view.findViewById(R.id.l2);
        l3=view.findViewById(R.id.l3);
        l4=view.findViewById(R.id.l4);
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
        l4.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 下面这些设置必须在此方法(onStart())中才有效
        window = getDialog().getWindow();
        if (window!=null){
            // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
            window.setBackgroundDrawableResource(android.R.color.transparent);
            // 设置动画
            window.setWindowAnimations(R.style.bottomDialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
            params.width = getResources().getDisplayMetrics().widthPixels;
            window.setAttributes(params);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.l1:

                break;
            case R.id.l2:

                break;
            case R.id.l3:

                break;
            case R.id.l4:

                break;
            case R.id.guanbi:
                dismiss();
                break;
        }
    }



}
