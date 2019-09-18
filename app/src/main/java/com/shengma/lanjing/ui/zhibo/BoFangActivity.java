package com.shengma.lanjing.ui.zhibo;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shengma.lanjing.MyApplication;
import com.shengma.lanjing.R;
import com.shengma.lanjing.adapters.GuanZhongAdapter;
import com.shengma.lanjing.adapters.LiWuBoFangAdapter;
import com.shengma.lanjing.adapters.LiaoTianAdapter;
import com.shengma.lanjing.beans.BaoCunBean;
import com.shengma.lanjing.beans.GuanZhongBean;
import com.shengma.lanjing.beans.LiWuBoFangBean;
import com.shengma.lanjing.beans.LiaoTianBean;
import com.shengma.lanjing.beans.LiwuPathBean;
import com.shengma.lanjing.beans.LiwuPathBean_;
import com.shengma.lanjing.beans.MsgWarp;
import com.shengma.lanjing.beans.QianBaoBean;
import com.shengma.lanjing.beans.XiaZaiLiWuBean;
import com.shengma.lanjing.beans.YongHuListBean;
import com.shengma.lanjing.dialogs.FenXiangDialog;
import com.shengma.lanjing.dialogs.InputPopupwindow;
import com.shengma.lanjing.dialogs.LiWuDialog;
import com.shengma.lanjing.dialogs.PKDialog;
import com.shengma.lanjing.dialogs.TuiChuDialog;
import com.shengma.lanjing.liveroom.IMLVBLiveRoomListener;
import com.shengma.lanjing.liveroom.MLVBLiveRoom;
import com.shengma.lanjing.liveroom.MLVBLiveRoomImpl;
import com.shengma.lanjing.liveroom.roomutil.commondef.AnchorInfo;
import com.shengma.lanjing.liveroom.roomutil.commondef.AudienceInfo;
import com.shengma.lanjing.utils.Consts;
import com.shengma.lanjing.utils.GsonUtil;
import com.shengma.lanjing.utils.InputMethodUtils;
import com.shengma.lanjing.utils.KeyboardStatusDetector;
import com.shengma.lanjing.utils.ToastUtils;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.query.LazyList;
import kotlin.Lazy;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class BoFangActivity extends AppCompatActivity implements IMLVBLiveRoomListener {
    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.xingguang)
    TextView xingguang;
    @BindView(R.id.guanzhongxiangqiang)
    TextView guanzhongxiangqiang;
    @BindView(R.id.paihangView)
    View paihangView;
    @BindView(R.id.fangjianhao)
    TextView fangjianhao;
    @BindView(R.id.tuichu)
    View tuichu;
    @BindView(R.id.fenxiang)
    View fenxiang;
    @BindView(R.id.fanzhuang)
    View fanzhuang;
    @BindView(R.id.meiyan)
    View meiyan;
    @BindView(R.id.pk)
    View pk;
    @BindView(R.id.shuodian)
    TextView shuodian;
    @BindView(R.id.liaotianReView)
    RecyclerView liaotianReView;
    @BindView(R.id.liwuReView)
    RecyclerView liwuReView;
    @BindView(R.id.donghua)
    ImageView donghua;
    private MLVBLiveRoom mlvbLiveRoom = MLVBLiveRoomImpl.sharedInstance(MyApplication.myApplication);
    private BaoCunBean baoCunBean = MyApplication.myApplication.getBaoCunBean();
    private TXCloudVideoView txCloudVideoView;    // 主播本地预览的 View
    private RecyclerView gz_recyclerView;
    private GuanZhongAdapter guanZhongAdapter;
    private List<GuanZhongBean> guanZhuBeanList = new ArrayList<>();
    private Timer timer = new Timer();
    private TimerTask task;
    private WeakHandler mHandler;
    private KeyboardStatusDetector keyboardStatusDetector;
    private InputPopupwindow popupwindow = null;
    private int width, hight;
    private List<LiaoTianBean> liaoTianBeanList = new ArrayList<>();
    private List<LiaoTianBean> lingshiList = new ArrayList<>();
    private LiaoTianAdapter liaoTianAdapter;
    // private List<RoomInfo> pkList=new ArrayList<>();
    // private PKListDialog pkListDialog;
    private int idid;
    private String playPath;
    private int jianpangHight;
    private List<LiWuBoFangBean> boFangBeanList = new ArrayList<>();
    private LiWuBoFangAdapter liWuBoFangAdapter;
    private LinkedBlockingQueue<Integer> linkedBlockingQueue;
    private TanChuangThread tanChuangThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_fang);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        MyApplication.myApplication.getYongHuListBeanBox().removeAll();
        playPath = getIntent().getStringExtra("playPath");
        idid = getIntent().getIntExtra("idid", 0);
        linkedBlockingQueue = new LinkedBlockingQueue<>();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;
        hight = outMetrics.heightPixels;
        liWuBoFangAdapter = new LiWuBoFangAdapter(boFangBeanList);
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                switch (message.what) {
                    case 111:

                        break;
                    case 222://聊天的
                        if (liaoTianBeanList.size() > 40) {
                            Iterator<LiaoTianBean> iter = liaoTianBeanList.iterator();
                            int i = 0;
                            while (iter.hasNext()) {
                                i++;
                                LiaoTianBean item = iter.next();
                                if (i < 10) {
                                    iter.remove();
                                } else {
                                    break;
                                }
                            }
                        }
                        liaoTianBeanList.addAll(lingshiList);
                        lingshiList.clear();
                        liaoTianAdapter.notifyDataSetChanged();

                        // Log.d("BoFangActivity", "更新聊天界面");
                        break;
                }
                return false;
            }
        });
        keyboardStatusDetector = new KeyboardStatusDetector(BoFangActivity.this);
        keyboardStatusDetector.registerView(shuodian);
        keyboardStatusDetector.setVisibilityListener(new KeyboardStatusDetector.KeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean keyboardVisible, int heightDiff) {
                jianpangHight = heightDiff;
//                if (keyboardVisible && isAA) {
//                    if (popupwindow != null)
//                        popupwindow.dismiss();
//                    popupwindow = new InputPopupwindow(BoFangActivity.this);
//                    popupwindow.setOutsideTouchable(true);
//                    if (!popupwindow.isShowing()){
//                        popupwindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, heightDiff);
//                    }
//                }else {
//                    if (popupwindow != null)
//                        popupwindow.dismiss();
//                }
            }
        });
        txCloudVideoView = findViewById(R.id.video_player);
        gz_recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BoFangActivity.this, LinearLayoutManager.HORIZONTAL, false);
        //设置布局管理器
        gz_recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        GuanZhongBean bean = new GuanZhongBean();
        bean.setXingguang(31321 + "");
        bean.setHeadImage(baoCunBean.getHeadImage());
        guanZhuBeanList.add(bean);
        guanZhuBeanList.add(bean);
        guanZhuBeanList.add(bean);
        guanZhuBeanList.add(bean);
        guanZhuBeanList.add(bean);
        guanZhuBeanList.add(bean);
        guanZhuBeanList.add(bean);
        guanZhuBeanList.add(bean);
        guanZhuBeanList.add(bean);
        guanZhongAdapter = new GuanZhongAdapter(guanZhuBeanList);
        gz_recyclerView.setAdapter(guanZhongAdapter);

        mlvbLiveRoom.setListener(this);
        // mlvbLiveRoom.setCameraMuteImage(BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish));
        String roomInfo = "";
        try {
            roomInfo = new JSONObject()
                    .put("title", "自定义")
                    .put("frontcover", "自定义")
                    .put("location", "自定义")
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mlvbLiveRoom.enterRoom(idid + "", txCloudVideoView, playPath, new EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Log.d("BoFangActivity", "errCode:" + errCode);
                Log.d("BoFangActivity", "errInfo:" + errInfo);
            }

            @Override
            public void onSuccess() {
                link_qianbao();//发送进房自定义消息
                Log.d("BoFangActivity", "加入直播间成功");

            }
        });
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(BoFangActivity.this, LinearLayoutManager.VERTICAL, true);
        //设置布局管理器
        liaotianReView.setLayoutManager(layoutManager1);
        //设置Adapter
        liaoTianAdapter = new LiaoTianAdapter(liaoTianBeanList);
        liaotianReView.setAdapter(liaoTianAdapter);
//        liaoTianAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
////            @Override public void onLoadMoreRequested() {
////                //mQuickAdapter.loadMoreEnd();mQuickAdapter.loadMoreComplete();mQuickAdapter.loadMoreFail();
////                liaoTianAdapter.loadMoreEnd();
////
////            }
////        }, liaotianReView);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(BoFangActivity.this, LinearLayoutManager.VERTICAL, true);
        liwuReView.setLayoutManager(layoutManager2);
        liwuReView.setAdapter(liWuBoFangAdapter);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) liaotianReView.getLayoutParams();
        params.height = (int) (hight * 0.33);
        liaotianReView.setLayoutParams(params);
        liaotianReView.invalidate();
        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) liwuReView.getLayoutParams();
        params2.height = (int) (hight * 0.33);
        liwuReView.setLayoutParams(params2);
        liwuReView.invalidate();

        tanChuangThread = new TanChuangThread();
        tanChuangThread.start();
        link_userinfo();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 222;
                mHandler.sendMessage(message);

            }
        };
        timer.schedule(task, 3000, 4000);
    }


    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {

    }

    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {

    }

    @Override
    public void onDebugLog(String log) {

    }

    @Override
    public void onRoomDestroy(String roomID) {

    }

    @Override
    public void onAnchorEnter(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAnchorExit(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {
        //观众进房
        Log.d("BoFangActivity", audienceInfo.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LiaoTianBean bean = new LiaoTianBean();
                bean.setNickname(audienceInfo.userName);
                bean.setType(2);
                bean.setUserInfo(audienceInfo.userInfo);
                bean.setHeadImage(audienceInfo.userAvatar);
                bean.setUserid(Long.parseLong(audienceInfo.userID));
                lingshiList.add(0, bean);
            }
        });

    }

    @Override
    public void onAudienceExit(AudienceInfo audienceInfo) {
        //观众退房
        Log.d("BoFangActivity", audienceInfo.toString());
//        LiaoTianBean bean=new LiaoTianBean();
//        bean.setNickname(audienceInfo.userName);
//        bean.setType(3);
//        bean.setUserInfo(audienceInfo.userInfo);
//        bean.setHeadImage(audienceInfo.userAvatar);
//        bean.setUserid(Long.parseLong(audienceInfo.userID));
    }

    @Override
    public void onRequestJoinAnchor(AnchorInfo anchorInfo, String reason) {

    }

    @Override
    public void onKickoutJoinAnchor() {

    }

    @Override
    public void onRequestRoomPK(AnchorInfo anchorInfo) {
        Log.d("ggggg", "收到PK请求");
        PKDialog pkDialog = new PKDialog(BoFangActivity.this, anchorInfo.userID);
        pkDialog.setOnQueRenListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拒绝
                pkDialog.dismiss();
                mlvbLiveRoom.responseRoomPK(anchorInfo.userID, false, "");

            }
        });
        pkDialog.setQuXiaoListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //同意
                pkDialog.dismiss();
                mlvbLiveRoom.responseRoomPK(anchorInfo.userID, true, "");
            }
        });
        pkDialog.show();
    }

    @Override
    public void onQuitRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {

    }

    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {
        switch (cmd) {
            case "1": //发送普通消息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LiaoTianBean bean = com.alibaba.fastjson.JSONObject.parseObject(message, LiaoTianBean.class);
                        lingshiList.add(0, bean);
                    }
                });
                break;
            case "liwudonghua1": //收到普通礼物消息
                synchronized (BoFangActivity.this) {
                    LiWuBoFangBean parseUser = com.alibaba.fastjson.JSONObject.parseObject(message, LiWuBoFangBean.class);
                    boFangBeanList.add(0, parseUser);
                    liWuBoFangAdapter.notifyDataSetChanged();
                    linkedBlockingQueue.offer(1);
                }
                break;
            case "liwudonghua2": //收到大型礼物消息


                break;
            case "rufang": //收到观众入房消息
                YongHuListBean yongHuListBean = com.alibaba.fastjson.JSONObject.parseObject(message, YongHuListBean.class);
                MyApplication.myApplication.getYongHuListBeanBox().put(yongHuListBean);
                break;

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxMSG(MsgWarp msgWarp) {
        if (msgWarp.getType() == 1005) {//收到输入的消息广播
            if (!msgWarp.getMsg().equals("")) {
                try {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    if (popupwindow != null)
                        popupwindow.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //发送消息
                Log.d("BoFangActivity", msgWarp.getMsg());
                LiaoTianBean bean = new LiaoTianBean();
                bean.setNickname(baoCunBean.getNickname());
                bean.setType(1);
                bean.setDengji(baoCunBean.getAnchorLevel());
                bean.setHeadImage(baoCunBean.getHeadImage());
                bean.setUserid(baoCunBean.getUserid());
                bean.setNeirong(msgWarp.getMsg());
                bean.setSex(baoCunBean.getSex());
                liaoTianBeanList.add(0, bean);
                liaoTianAdapter.notifyDataSetChanged();
                String js = com.alibaba.fastjson.JSONObject.toJSONString(bean);
                mlvbLiveRoom.sendRoomCustomMsg("1", js, new SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        Log.d("BoFangActivity", "errCode:" + errCode);
                        Log.d("BoFangActivity", "errInfo:" + errInfo);
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("BoFangActivity", "发送自定义消息成功1");
                    }
                });

            }
        }

        if (msgWarp.getType() == 1100) {//发送礼物之后的广播1.播放自己的礼物动画，2发送自定义消息，让其他人播放礼物动画
            Log.d("BoFangActivity", "msgWarp.getTemp()" + msgWarp.getTemp());
            if (Integer.parseInt(msgWarp.getTemp()) == 0) {
                //礼物类型1
                LiWuBoFangBean liWuBoFangBean = new LiWuBoFangBean();
                liWuBoFangBean.setHeadImage(baoCunBean.getHeadImage());
                liWuBoFangBean.setId(baoCunBean.getUserid());
                liWuBoFangBean.setName(baoCunBean.getNickname());
                XiaZaiLiWuBean nliwuname = MyApplication.myApplication.getXiaZaiLiWuBeanBox().get(Integer.parseInt(msgWarp.getMsg()));
                if (nliwuname == null) {
                    ToastUtils.showError(BoFangActivity.this, "未找到本地礼物");
                    return;
                }
                liWuBoFangBean.setLiwuName(nliwuname.getGiftName());
                liWuBoFangBean.setLiwuPath(nliwuname.getGiftUrl());
                String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(liWuBoFangBean);
                mlvbLiveRoom.sendRoomCustomMsg("liwudonghua1", jsonString, new SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        Log.d("BoFangActivity", "发送礼物自定义消息失败" + errInfo + errCode);
                        ToastUtils.showError(BoFangActivity.this, "发送礼物消息失败");
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("BoFangActivity", "发送礼物自定义消息成功");
                        synchronized (BoFangActivity.this) {
                            boFangBeanList.add(0, liWuBoFangBean);
                            liWuBoFangAdapter.notifyDataSetChanged();
                            linkedBlockingQueue.offer(1);
                        }

                    }
                });

            } else { //礼物类型2
                LiWuBoFangBean liWuBoFangBean = new LiWuBoFangBean();
                liWuBoFangBean.setHeadImage(baoCunBean.getHeadImage());
                liWuBoFangBean.setId(baoCunBean.getUserid());
                liWuBoFangBean.setName(baoCunBean.getNickname());
                XiaZaiLiWuBean nliwuname = MyApplication.myApplication.getXiaZaiLiWuBeanBox().get(Integer.parseInt(msgWarp.getMsg()));
                if (nliwuname == null) {
                    ToastUtils.showError(BoFangActivity.this, "未找到本地礼物");
                    return;
                }
                liWuBoFangBean.setLiwuName(nliwuname.getGiftName());
                liWuBoFangBean.setLiwuPath(nliwuname.getGiftUrl());
                String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(liWuBoFangBean);
                mlvbLiveRoom.sendRoomCustomMsg("liwudonghua2", jsonString, new SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        Log.d("BoFangActivity", "发送礼物自定义消息失败" + errInfo + errCode);
                        ToastUtils.showError(BoFangActivity.this, "发送礼物消息失败");
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("BoFangActivity", "发送礼物自定义消息成功2");
                        //播放.query().equal(Subject_.teZhengMa, new String(result.faceToken)).build()

                        LazyList<LiwuPathBean> list=MyApplication.myApplication.getLiwuPathBeanBox().query().equal(LiwuPathBean_.sid,nliwuname.getId())
                                .build().findLazy();
                        if (list.size()==0){
                            ToastUtils.showError(BoFangActivity.this, "未找到礼物资源,请等待后台下载完成");
                        }
                        AnimationDrawable animationDrawable = new AnimationDrawable();
                        animationDrawable.setOneShot(true);//播放一次
                        int duration = 0;
                        for (LiwuPathBean bean:list) {
                            Drawable drawable = Drawable.createFromPath(bean.getPath());
                            if (drawable!=null){
                                duration+=200;
                                animationDrawable.addFrame(drawable, 200);
                                Log.d("BoFangActivity", "drawable");
                            }
                        }
                        donghua.setBackground(animationDrawable);
                        animationDrawable.start();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {//动画结束
                                donghua.setBackground(null);
                            }
                        }, duration);

                    }
                });
            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        BaoCunBean baoCunBean = MyApplication.myApplication.getBaoCunBean();
        if (baoCunBean != null) {
            xingguang.setText("0");
            name.setText(baoCunBean.getNickname());
            Glide.with(BoFangActivity.this)
                    .load(baoCunBean.getHeadImage())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(touxiang);
        }


    }


    @Override
    protected void onDestroy() {
        timer.cancel();
        //退出时发送退房自定义通知
        YongHuListBean yongHuListBean = new YongHuListBean();
        yongHuListBean.setId(baoCunBean.getUserid());
        String js = com.alibaba.fastjson.JSONObject.toJSONString(yongHuListBean);
        mlvbLiveRoom.sendRoomCustomMsg("tuifang", js, new SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
            }

            @Override
            public void onSuccess() {
            }
        });

        if (task != null)
            task.cancel();
        if (tanChuangThread != null)
            tanChuangThread.interrupt();

        EventBus.getDefault().unregister(this);

        super.onDestroy();


    }

    @Override
    public void onBackPressed() {

        TuiChuDialog tuiChuDialog = new TuiChuDialog(BoFangActivity.this);
        tuiChuDialog.setOnQueRenListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlvbLiveRoom.exitRoom(new ExitRoomCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        tuiChuDialog.dismiss();
                        BoFangActivity.this.finish();
                    }

                    @Override
                    public void onSuccess() {
                        tuiChuDialog.dismiss();
                        BoFangActivity.this.finish();
                    }
                });
            }
        });
        tuiChuDialog.setQuXiaoListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tuiChuDialog.dismiss();
            }
        });
        tuiChuDialog.setCanceledOnTouchOutside(false);
        tuiChuDialog.show();
        //  super.onBackPressed();
    }

    @OnClick({R.id.guanzhongxiangqiang, R.id.paihangView, R.id.tuichu, R.id.fenxiang,
            R.id.fanzhuang, R.id.meiyan, R.id.pk, R.id.shuodian, R.id.video_player})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guanzhongxiangqiang:

                break;
            case R.id.paihangView:

                break;
            case R.id.tuichu:
                TuiChuDialog tuiChuDialog = new TuiChuDialog(BoFangActivity.this);
                tuiChuDialog.setOnQueRenListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mlvbLiveRoom.exitRoom(new ExitRoomCallback() {
                            @Override
                            public void onError(int errCode, String errInfo) {
                                tuiChuDialog.dismiss();
                                BoFangActivity.this.finish();
                            }

                            @Override
                            public void onSuccess() {
                                tuiChuDialog.dismiss();
                                BoFangActivity.this.finish();
                            }
                        });
                    }
                });
                tuiChuDialog.setQuXiaoListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tuiChuDialog.dismiss();
                    }
                });
                tuiChuDialog.setCanceledOnTouchOutside(false);
                tuiChuDialog.show();
                break;
            case R.id.fenxiang:
                FenXiangDialog dialog = new FenXiangDialog();
                dialog.show(getSupportFragmentManager(), "fenxiang");
                break;
            case R.id.fanzhuang:
                mlvbLiveRoom.switchCamera();
                break;
            case R.id.meiyan:
                //美颜是转盘

                break;
            case R.id.pk:
                //pk是礼物
                LiWuDialog liWuDialog = new LiWuDialog(idid + "");
                liWuDialog.show(getSupportFragmentManager(), "liWuDialog");

                break;
            case R.id.video_player:
                try {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    if (popupwindow != null)
                        popupwindow.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.shuodian:
                InputMethodUtils.showOrHide(this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(200);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (popupwindow != null)
                                    popupwindow.dismiss();
                                popupwindow = new InputPopupwindow(BoFangActivity.this);
                                popupwindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, jianpangHight);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    private void link_userinfo() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = null;
//        body = new FormBody.Builder()
//                .add("uname", uname)
//                .add("pwd", pwd)
//                .build();


//        JSONObject object=new JSONObject();
//        try {
//            object.put("uname",uname);
//            object.put("pwd",pwd);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //RequestBody body = RequestBody.create(object.toString(),JSON);
//        RequestBody fileBody = RequestBody.create(new File(path), MediaType.parse("application/octet-stream"));
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("img", System.currentTimeMillis() + ".png", fileBody)
//                .build();
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + MyApplication.myApplication.getBaoCunBean().getSession())
                .get()
                .url(Consts.URL + "/user/info");
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
                    Log.d("AllConnects", "用户主页信息:" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
//                    Gson gson = new Gson();
//                    LogingBe logingBe = gson.fromJson(jsonObject, LogingBe.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Glide.with(BoFangActivity.this)
//                                    .load(path)
//                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                                    .into(touxiang);
                        }
                    });
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    // ToastUtils.showError(BoFangActivity.this, "获取数据失败");

                }
            }
        });
    }


    private void link_qianbao() {
        //  MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = null;
        body = new FormBody.Builder()
                .add("", "")
                .build();
//        JSONObject object=new JSONObject();
//        try {
//            object.put("uname",uname);
//            object.put("pwd",pwd);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //RequestBody body = RequestBody.create(object.toString(),JSON);
//        RequestBody fileBody = RequestBody.create(new File(path), MediaType.parse("application/octet-stream"));
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("img", System.currentTimeMillis() + ".png", fileBody)
//                .build();
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + MyApplication.myApplication.getBaoCunBean().getSession())
                .post(body)
                .url(Consts.URL + "/user/wallet");
        // step 3：创建 Call 对象
        Call call = MyApplication.myApplication.getOkHttpClient().newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                ToastUtils.showError(BoFangActivity.this, "进房失败,请退出后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "钱包:" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    QianBaoBean logingBe = gson.fromJson(jsonObject, QianBaoBean.class);
                    if (logingBe.getCode() == 2000) {
                        YongHuListBean yongHuListBean = new YongHuListBean();
                        yongHuListBean.setName(baoCunBean.getNickname());
                        yongHuListBean.setDengji(baoCunBean.getAnchorLevel());
                        yongHuListBean.setHeadImage(baoCunBean.getHeadImage());
                        yongHuListBean.setId(baoCunBean.getUserid());
                        yongHuListBean.setJingbi(logingBe.getResult().getConsumption());
                        yongHuListBean.setSex(baoCunBean.getSex());
                        yongHuListBean.setType(0);
                        String js = com.alibaba.fastjson.JSONObject.toJSONString(yongHuListBean);
                        // Log.d("BoFangActivity", js);
                        mlvbLiveRoom.sendRoomCustomMsg("rufang", js, new SendRoomCustomMsgCallback() {
                            @Override
                            public void onError(int errCode, String errInfo) {
                                ToastUtils.showError(BoFangActivity.this, "发送进房消息失败,请退出后重试");
                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
                    } else {
                        ToastUtils.showError(BoFangActivity.this, "进房失败,请退出后重试");
                    }
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    ToastUtils.showError(BoFangActivity.this, "进房失败,请退出后重试");

                }
            }
        });
    }

    private class TanChuangThread extends Thread {
        boolean isRing;

        @Override
        public void run() {
            while (!isRing) {
                try {

                    //有动画 ，延迟到一秒一次
                    Integer subject = linkedBlockingQueue.take();
                    SystemClock.sleep(4000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (BoFangActivity.this) {
                                boFangBeanList.remove(boFangBeanList.size() - 1);
                                liWuBoFangAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            isRing = true;
            // Log.d("RecognizeThread", "中断了弹窗线程");
            super.interrupt();
        }
    }

}
