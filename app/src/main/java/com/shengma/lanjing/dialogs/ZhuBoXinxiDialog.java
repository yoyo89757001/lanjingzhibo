package com.shengma.lanjing.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shengma.lanjing.MyApplication;
import com.shengma.lanjing.R;
import com.shengma.lanjing.beans.ChaXunGeRenXinXi;
import com.shengma.lanjing.utils.Consts;
import com.shengma.lanjing.utils.GsonUtil;
import com.shengma.lanjing.utils.Utils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ZhuBoXinxiDialog extends DialogFragment  {

    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.guanbi)
    ImageView guanbi;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.xingbie)
    ImageView xingbie;
    @BindView(R.id.dengji)
    TextView dengji;
    @BindView(R.id.dengji2)
    TextView dengji2;
    @BindView(R.id.myid)
    TextView myid;
    @BindView(R.id.fensi)
    TextView fensi;
    @BindView(R.id.guanzhu)
    TextView guanzhu;
    @BindView(R.id.textView2)
    LinearLayout textView2;
    @BindView(R.id.xingguang)
    TextView xingguang;
    private Window window;
    private Unbinder unbinder;
    private String id;

    public ZhuBoXinxiDialog(String id) {
        this.id=id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.zhuboxinxi_dialog, null);
        unbinder = ButterKnife.bind(this, view);
        guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        link_userinfo(id);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 下面这些设置必须在此方法(onStart())中才有效
        window = getDialog().getWindow();
        if (window != null) {
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



    private void link_userinfo(String id) {
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + MyApplication.myApplication.getBaoCunBean().getSession())
                .get()
                .url(Consts.URL + "/anchor/info/"+id);
        // step 3：创建 Call 对象
        Call call = MyApplication.myApplication.getOkHttpClient().newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                //  ToastUtils.showError(WoDeZiLiaoActivity.this, "获取数据失败,请检查网络");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "查询个人信息:" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    ChaXunGeRenXinXi logingBe = gson.fromJson(jsonObject, ChaXunGeRenXinXi.class);
                    if (logingBe.getCode()==2000) {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    name.setText(logingBe.getResult().getNickname());
                                    dengji.setText("Lv."+logingBe.getResult().getAnchorLevel());
                                    dengji2.setText("Lv."+logingBe.getResult().getUserLevel());
                                    if (logingBe.getResult().getSex()==1){
                                        xingbie.setBackgroundResource(R.drawable.nan);
                                    }else {
                                        xingbie.setBackgroundResource(R.drawable.nv);
                                    }
                                    fensi.setText(logingBe.getResult().getFans()+"");
                                    guanzhu.setText(logingBe.getResult().getIdols()+"");
                                    double xg = logingBe.getResult().getStarLight();
                                    if (xg>=10000){
                                        xingguang.setText(Utils.doubleToString(xg/10000.0)+"万");
                                    }else {
                                        xingguang.setText(xg+ "");
                                    }
                                    Glide.with(getActivity())
                                            .load(logingBe.getResult().getHeadImage())
                                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                            .into(touxiang);
                                }
                            });
                    }
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    // ToastUtils.showError(BoFangActivity.this, "获取数据失败");

                }
            }
        });
    }

}
