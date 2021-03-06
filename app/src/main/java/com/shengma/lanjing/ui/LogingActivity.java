package com.shengma.lanjing.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.shengma.lanjing.MyApplication;
import com.shengma.lanjing.R;
import com.shengma.lanjing.beans.BaoCunBean;
import com.shengma.lanjing.beans.LogingBe;
import com.shengma.lanjing.beans.MsgWarp;
import com.shengma.lanjing.cookies.CookiesManager;
import com.shengma.lanjing.utils.Consts;
import com.shengma.lanjing.utils.GsonUtil;
import com.shengma.lanjing.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LogingActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks  {
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .writeTimeout(18000, TimeUnit.MILLISECONDS)
            .connectTimeout(18000, TimeUnit.MILLISECONDS)
            .readTimeout(18000, TimeUnit.MILLISECONDS)
				    .cookieJar(new CookiesManager())
            //        .retryOnConnectionFailure(true)
            .build();

    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.yingcang)
    ImageView yingcang;
    @BindView(R.id.yanzhengmadenglu)
    TextView yanzhengmadenglu;
    @BindView(R.id.weixin)
    ImageView weixin;
    @BindView(R.id.denglu)
    Button denglu;
    private static final String APP_ID = "wxa118e3198ef780bd";
    ZLoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        String aaa=getIntent().getStringExtra("aaa");
        methodRequiresTwoPermission();
        if (aaa==null){
            if (MyApplication.myApplication.getBaoCunBean().getSession()!=null){
                  startActivity(new Intent(LogingActivity.this, MainActivity.class));
                  finish();
            }
        }
       //link_loging("18620124189","yoyo89757");
      //  link_loging("16620954033","123456");

    }




    private boolean isC=false;

    @OnClick({R.id.yingcang, R.id.yanzhengmadenglu, R.id.weixin,R.id.denglu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.yingcang:
                if (!isC)  {
                    isC=true;
                    pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    isC=false;
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                break;
            case R.id.yanzhengmadenglu:
                startActivity(new Intent(LogingActivity.this,LogingActivity_zhuce.class));

                break;
            case R.id.weixin:

                IWXAPI api = WXAPIFactory.createWXAPI(this, APP_ID, true);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_denglu";
                api.sendReq(req);

                break;
            case R.id.denglu:
                denglu.setEnabled(false);
                dialog = new ZLoadingDialog(LogingActivity.this);
                dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型
                        .setLoadingColor(Color.parseColor("#FF3EE1F7"))//颜色
                        .setHintText("加载中...")
                        .setHintTextSize(16) // 设置字体大小 dp
                        .setHintTextColor(Color.WHITE)  // 设置字体颜色
                        .setDurationTime(0.6) // 设置动画时间百分比 - 0.5倍
                        .setDialogBackgroundColor(Color.parseColor("#bb111111")) // 设置背景色，默认白色
                        .show();
                link_loging(phone.getText().toString().trim(),pwd.getText().toString().trim());
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxMSG(MsgWarp msgWarp){
        if (msgWarp.getType()==1001){//微信传送
            Log.d("LogingActivity", msgWarp.getMsg());
            dialog = new ZLoadingDialog(LogingActivity.this);
            dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型
                    .setLoadingColor(Color.parseColor("#FF3EE1F7"))//颜色
                    .setHintText("加载中...")
                    .setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.WHITE)  // 设置字体颜色
                    .setDurationTime(0.6) // 设置动画时间百分比 - 0.5倍
                    .setDialogBackgroundColor(Color.parseColor("#bb111111")) // 设置背景色，默认白色
                    .show();
                link_WX1(msgWarp.getMsg());

        }else if (msgWarp.getType()==-1001){
            ToastUtils.showError(LogingActivity.this,msgWarp.getMsg());
        }

        if (msgWarp.getType()==1002){
            finish();
        }
    }

    private void link_logout() {
        // MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = null;
        body = new FormBody.Builder()
                .add("uname", "")
                .build();
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie","JSESSIONID="+ MyApplication.myApplication.getBaoCunBean().getSession())
                .post(body)
                .url(Consts.URL+"/logout");
        // step 3：创建 Call 对象
        Call call = MyApplication.myApplication.getOkHttpClient().newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    // JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Log.d("AllConnects", "退出:" + ss);
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");

                }
            }
        });
    }

    private void link_loging(String uname,String pwd) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = null;
//        body = new FormBody.Builder()
//                .add("uname", uname)
//                .add("pwd", pwd)
//                .build();

//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("username", "")
//                .addFormDataPart("password", "")
//                .build();
        JSONObject object=new JSONObject();
        try {
            object.put("uname",uname);
            object.put("pwd",pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(),JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(Consts.URL+"/login/password");
        // step 3：创建 Call 对象
        Call call = MyApplication.myApplication.getOkHttpClient().newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                ToastUtils.showError(LogingActivity.this,"获取数据失败,请检查网络");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog!=null)
                            dialog.dismiss();
                        denglu.setEnabled(true);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog!=null)
                            dialog.dismiss();
                        denglu.setEnabled(true);
                    }
                });
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    LogingBe logingBe = gson.fromJson(jsonObject, LogingBe.class);
                    if (logingBe.getCode()==2000) {
                        Box<BaoCunBean> baoCunBeanBox = MyApplication.myApplication.getBaoCunBeanBox();
                        BaoCunBean bean = baoCunBeanBox.get(123456);
                        bean.setImUserSig(logingBe.getResult().getImUserSig());
                        bean.setSdkAppId(logingBe.getResult().getSdkAppId());
                        bean.setIsBind(logingBe.getResult().getIsBind());
                        bean.setSession(logingBe.getResult().getSession());
                        baoCunBeanBox.put(bean);
                        startActivity(new Intent(LogingActivity.this, MainActivity.class));
                        finish();
                    }else if (logingBe.getCode()==-1|| logingBe.getCode()==-2 || logingBe.getCode()==-3){
                        ToastUtils.showInfo(LogingActivity.this,"账号或密码错误");
                    }else {
                        ToastUtils.showInfo(LogingActivity.this,logingBe.getDesc());
                    }
                    Log.d("AllConnects", "登录:" + ss);
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    ToastUtils.showError(LogingActivity.this,"获取数据失败");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog!=null)
                                dialog.dismiss();
                        }
                    });

                }
            }
        });
    }



    private void link_WX1(String code) {
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .get()
                .url("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
                        Consts.APP_ID+"&secret=a9c7c1bdaf1e7e3ecd90339d5c8cbf7d&code=" +
                        code+"&grant_type=authorization_code");
        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                ToastUtils.showError(LogingActivity.this,"获取数据失败,请检查网络");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog!=null)
                            dialog.dismiss();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("LogingActivity", "sssssssss"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    link_WX2(jsonObject.get("access_token").getAsString(),jsonObject.get("openid").getAsString());

                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    ToastUtils.showError(LogingActivity.this,"获取数据失败");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog!=null)
                                dialog.dismiss();
                        }
                    });
                }
            }
        });
    }


    private void link_WX2(String access_token,String openid) {

        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .get()
                .url("https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token+"&openid=" +openid);

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                ToastUtils.showError(LogingActivity.this,"获取数据失败,请检查网络");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog!=null)
                            dialog.dismiss();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("LogingActivity", "sssssssss"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    link_wx(jsonObject.get("openid").getAsString(),jsonObject.get("nickname").getAsString(),
                            jsonObject.get("sex").getAsInt(), jsonObject.get("headimgurl").getAsString(),
                            jsonObject.get("unionid").getAsString());
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    ToastUtils.showError(LogingActivity.this,"获取数据失败");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog!=null)
                                dialog.dismiss();
                        }
                    });
                }
            }

        });
    }

    private void link_wx(String openid,String nickname,int sex,String headimgurl,String unionid) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = null;
//        body = new FormBody.Builder()
//                .add("uname", uname)
//                .add("pwd", pwd)
//                .build();

//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("username", "")
//                .addFormDataPart("password", "")
//                .build();
        if (headimgurl.equals("")){
            headimgurl="https://www.1.com";
        }
        JSONObject object=new JSONObject();
        try {
            object.put("openId",openid);
            object.put("nickname",nickname);
            object.put("sex",sex);
            object.put("headImg",headimgurl);
            object.put("unionId",unionid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("LogingActivity", object.toString());
        RequestBody body = RequestBody.create(object.toString(),JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(Consts.URL+"/login/wechat");
        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                ToastUtils.showError(LogingActivity.this,"获取数据失败,请检查网络");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog!=null)
                            dialog.dismiss();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog!=null)
                            dialog.dismiss();
                    }
                });
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("LogingActivity", "登录我们的微信"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    LogingBe logingBe = gson.fromJson(jsonObject, LogingBe.class);
                    if (logingBe.getCode()==2000) {
                        Box<BaoCunBean> baoCunBeanBox = MyApplication.myApplication.getBaoCunBeanBox();
                        BaoCunBean bean = baoCunBeanBox.get(123456);
                        bean.setImUserSig(logingBe.getResult().getImUserSig());
                        bean.setSdkAppId(logingBe.getResult().getSdkAppId());
                        bean.setIsBind(logingBe.getResult().getIsBind());
                        bean.setSession(logingBe.getResult().getSession());
                        baoCunBeanBox.put(bean);
                        if (logingBe.getResult().getIsBind().equals("1")){
                            startActivity(new Intent(LogingActivity.this, MainActivity.class));
                        }else {//去绑定手机界面
                            Intent intent =new Intent(LogingActivity.this, LogingActivity_zhuce.class);
                            intent.putExtra("wx","wx");
                            startActivity(intent);
                        }

                    }else {
                        ToastUtils.showInfo(LogingActivity.this,logingBe.getDesc());
                    }
                    Log.d("AllConnects", "登录:" + ss);
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    ToastUtils.showError(LogingActivity.this,"获取数据失败");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog!=null)
                                dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private final int RC_CAMERA_AND_LOCATION=10000;

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
                Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.WAKE_LOCK,
                Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.INTERNET};

        if (EasyPermissions.hasPermissions(this, perms)) {
            // 已经得到许可，就去做吧 //第一次授权成功也会走这个方法
            Log.d("BaseActivity", "成功获得权限");
            File file = new File(MyApplication.SDPATH);
            if (!file.exists()) {
                Log.d("ggg", "file.mkdirs():" + file.mkdirs());
            }

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "需要授予app权限,请点击确定",
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        Log.d("BaseActivity", "list.size():" + list.size());

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
        for (String s:list){
            Log.d("BaseActivity", s);
        }
        Log.d("BaseActivity", "list.size():" + list.size());
        Toast.makeText(LogingActivity.this,"权限被拒绝会导致无法正常使用app",Toast.LENGTH_LONG).show();
        // finish();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }




}
