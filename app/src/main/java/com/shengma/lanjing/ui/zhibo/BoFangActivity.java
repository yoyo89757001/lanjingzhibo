package com.shengma.lanjing.ui.zhibo;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieImageAsset;
import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shengma.lanjing.MyApplication;
import com.shengma.lanjing.R;
import com.shengma.lanjing.adapters.GuanZhongAdapter;
import com.shengma.lanjing.adapters.LiWuBoFangAdapter;
import com.shengma.lanjing.adapters.LiaoTianAdapter;
import com.shengma.lanjing.beans.BaoCunBean;
import com.shengma.lanjing.beans.ChaXunGeRenXinXi;
import com.shengma.lanjing.beans.LiWuBoFangBean;
import com.shengma.lanjing.beans.LiaoTianBean;
import com.shengma.lanjing.beans.MsgWarp;
import com.shengma.lanjing.beans.PuTongInfio;
import com.shengma.lanjing.beans.XiaZaiLiWuBean;
import com.shengma.lanjing.beans.YongHuListBean;
import com.shengma.lanjing.beans.YongHuListBean_;
import com.shengma.lanjing.dialogs.FenXiangDialog;
import com.shengma.lanjing.dialogs.InputPopupwindow;
import com.shengma.lanjing.dialogs.LiWuDialog;
import com.shengma.lanjing.dialogs.PaiHangListDialog;
import com.shengma.lanjing.dialogs.TuiChuDialog;
import com.shengma.lanjing.dialogs.YongHuListDialog;
import com.shengma.lanjing.dialogs.YongHuXinxiDialog;
import com.shengma.lanjing.dialogs.ZhuBoXinxiDialog;
import com.shengma.lanjing.dialogs.ZhuanPanDialog;
import com.shengma.lanjing.liveroom.IMLVBLiveRoomListener;
import com.shengma.lanjing.liveroom.MLVBLiveRoom;
import com.shengma.lanjing.liveroom.MLVBLiveRoomImpl;
import com.shengma.lanjing.liveroom.roomutil.commondef.AnchorInfo;
import com.shengma.lanjing.liveroom.roomutil.commondef.AudienceInfo;
import com.shengma.lanjing.utils.Consts;
import com.shengma.lanjing.utils.GsonUtil;
import com.shengma.lanjing.utils.InputMethodUtils;
import com.shengma.lanjing.utils.KeyboardStatusDetector;
import com.shengma.lanjing.utils.ReadAssetsJsonUtil;
import com.shengma.lanjing.utils.ToastUtils;
import com.shengma.lanjing.utils.Utils;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
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
import io.objectbox.Box;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;


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
    @BindView(R.id.guanzhu)
    ImageView guanzhu;
    @BindView(R.id.paiming)
    TextView paiming;
    @BindView(R.id.group)
    Group group;
    @BindView(R.id.daojishi)
    TextView daojishi;
    @BindView(R.id.pkview1)
    View pkview1;
    @BindView(R.id.pkview2)
    View pkview2;
    @BindView(R.id.pktv1)
    TextView pktv1;
    @BindView(R.id.pktv2)
    TextView pktv2;
    @BindView(R.id.chengfa)
    TextView chengfa;
    @BindView(R.id.toptop)
    View toptop;
    @BindView(R.id.pkjgim1)
    ImageView pkjgim1;
    @BindView(R.id.pkjgim2)
    ImageView pkjgim2;
    @BindView(R.id.rootv)
    LinearLayout rootv;

    private MLVBLiveRoom mlvbLiveRoom = MLVBLiveRoomImpl.sharedInstance(MyApplication.myApplication);
    private BaoCunBean baoCunBean = MyApplication.myApplication.getBaoCunBean();
    private TXCloudVideoView txCloudVideoView;    // 主播本地预览的 View
    private RecyclerView gz_recyclerView;
    private GuanZhongAdapter guanZhongAdapter;
    private List<YongHuListBean> guanZhongBeanList = new ArrayList<>();
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
    private long idid;
    private String playPath;
    private int jianpangHight;
    private List<LiWuBoFangBean> boFangBeanList = new ArrayList<>();
    private LiWuBoFangAdapter liWuBoFangAdapter;
    private LinkedBlockingQueue<Integer> linkedBlockingQueue;
    private LinkedBlockingQueue<String> linkedBlockingQueueLW;
    private TanChuangThread tanChuangThread;
    private TanChuangThreadLW tanChuangThreadLW;
    private LottieAnimationView animationView;
    private Box<YongHuListBean> yongHuListBeanBox = MyApplication.myApplication.getYongHuListBeanBox();
    private boolean isGuanzhu = false;
    private long numberGZ;
    private int heightPixels, widthPixels;
    private static CountDownTimer timer1;//pk
    private static CountDownTimer timer2;//惩罚
    private float pktWidth;
    private boolean isON = true, isGLY = false;
    private Box<XiaZaiLiWuBean> xiaZaiLiWuBeanBox = MyApplication.myApplication.getXiaZaiLiWuBeanBox();
    private int updateCoune = 0;
    private boolean isJY = false;
    protected InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_bo_fang);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        imm = (InputMethodManager) BoFangActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        animationView = findViewById(R.id.animation_view);
        MyApplication.myApplication.getYongHuListBeanBox().removeAll();
        playPath = getIntent().getStringExtra("playPath");
        idid = getIntent().getLongExtra("idid", 0);
        link_isguanzhu(idid + "");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixels = displayMetrics.heightPixels;
        widthPixels = displayMetrics.widthPixels;
        linkedBlockingQueue = new LinkedBlockingQueue<>();
        linkedBlockingQueueLW = new LinkedBlockingQueue<>();
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
                        liaoTianBeanList.addAll(0, lingshiList);
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
        guanZhongAdapter = new GuanZhongAdapter(guanZhongBeanList);
        guanZhongAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ZhuBoXinxiDialog zhuBoXinxiDialog = new ZhuBoXinxiDialog(guanZhongBeanList.get(position).getId() + "");
                zhuBoXinxiDialog.show(getSupportFragmentManager(), "dfrttt");
            }
        });
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

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(BoFangActivity.this, LinearLayoutManager.VERTICAL, true);
        //设置布局管理器
        liaotianReView.setLayoutManager(layoutManager1);
        //设置Adapter
        liaoTianAdapter = new LiaoTianAdapter(liaoTianBeanList);
        liaoTianAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                // Log.d("ZhiBoActivity", "长按");
                if (isGLY && !(baoCunBean.getUserid() + "").equals(liaoTianBeanList.get(position).getUserid() + "")) {
                    YongHuXinxiDialog yongHuXinxiDialog = new YongHuXinxiDialog(idid + "", liaoTianBeanList.get(position).getUserid() + "", true);
                    yongHuXinxiDialog.show(getSupportFragmentManager(), "yonghuxnxi");
                } else {
                    YongHuXinxiDialog yongHuXinxiDialog = new YongHuXinxiDialog(idid + "", liaoTianBeanList.get(position).getUserid() + "", false);
                    yongHuXinxiDialog.show(getSupportFragmentManager(), "yonghuxnxi");
                }
                return false;
            }
        });
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
        params.height = (int) (hight * 0.30);
        liaotianReView.setLayoutParams(params);
        liaotianReView.invalidate();
        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) liwuReView.getLayoutParams();
        params2.height = (int) (hight * 0.33);
        liwuReView.setLayoutParams(params2);
        liwuReView.invalidate();

        ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams) txCloudVideoView.getLayoutParams();
        params3.height = heightPixels;
        txCloudVideoView.setLayoutParams(params3);
        txCloudVideoView.invalidate();

        mlvbLiveRoom.enterRoom(idid + "", txCloudVideoView, playPath, new EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Log.d("BoFangActivity", "errCode:" + errCode);
                Log.d("BoFangActivity", "errInfo:" + errInfo);
                ToastUtils.showInfo(BoFangActivity.this, "进房失败,主播已经下线了");
            }

            @Override
            public void onSuccess() {
                link_qianbao();//发送进房自定义消息
                Log.d("BoFangActivity", "加入直播间成功");
                LiaoTianBean bean = new LiaoTianBean();
                bean.setType(2);
                bean.setNickname("");
                bean.setNeirong(" 蓝鲸倡导绿色直播环境，对直播内容会24小时巡查，封面和直播内容有任何违法违规、色情暴力、抹黑诋毁、低俗不良行为将被禁封。传播正能量，从自我做起！");
                liaoTianBeanList.add(bean);
                liaoTianAdapter.notifyDataSetChanged();
            }
        });
        tanChuangThread = new TanChuangThread();
        tanChuangThread.start();
        tanChuangThreadLW = new TanChuangThreadLW();
        tanChuangThreadLW.start();

        link_userinfo();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 222;
                mHandler.sendMessage(message);
            }
        };
        timer.schedule(task, 3000, 3000);
        //link_chaxunJY(idid+"",baoCunBean.getUserid()+"");
    }


    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {
        Log.d("BoFangActivity", "errCode:" + errCode);
        Log.d("BoFangActivity", errMsg);
        //  Log.d("BoFangActivity", extraInfo.toString()+"dddd");
        if (errCode == -2301) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TuiChuDialog tuiChuDialog = new TuiChuDialog(BoFangActivity.this);
                    tuiChuDialog.setTextView("主播已下线,要退出吗?");
                    tuiChuDialog.setCanceledOnTouchOutside(false);
                    tuiChuDialog.setOnQueRenListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tuiChuDialog.dismiss();
                            finish();
                        }
                    });
                    tuiChuDialog.setQuXiaoListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tuiChuDialog.dismiss();
                        }
                    });
                    tuiChuDialog.show();
                }
            });
        }
        //
    }

    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {

    }

    @Override
    public void onDebugLog(String log) {

    }

    @Override
    public void onRoomDestroy(String roomID) {
        Log.d("BoFangActivity", roomID + "是退房吗2");
    }

    @Override
    public void onAnchorEnter(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAnchorExit(AnchorInfo anchorInfo) {
        Log.d("BoFangActivity", anchorInfo.userID + "是退房吗");

    }

    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {
        //观众进房
        Log.d("BoFangActivity", audienceInfo.toString());
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                LiaoTianBean bean = new LiaoTianBean();
//                bean.setNickname(audienceInfo.userName);
//                bean.setType(2);
//                bean.setUserInfo(audienceInfo.userInfo);
//                bean.setHeadImage(audienceInfo.userAvatar);
//                bean.setUserid(Long.parseLong(audienceInfo.userID));
//                lingshiList.add(0, bean);
//            }
//        });

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
//        Log.d("ggggg", "收到PK请求");
//        PKDialog pkDialog = new PKDialog(BoFangActivity.this, anchorInfo.userID);
//        pkDialog.setOnQueRenListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //拒绝
//                pkDialog.dismiss();
//                mlvbLiveRoom.responseRoomPK(anchorInfo.userID, false, "");
//
//            }
//        });
//        pkDialog.setQuXiaoListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //同意
//                pkDialog.dismiss();
//                mlvbLiveRoom.responseRoomPK(anchorInfo.userID, true, "");
//            }
//        });
//        pkDialog.show();
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
            case "1": //普通消息
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
                LiWuBoFangBean parseUser = com.alibaba.fastjson.JSONObject.parseObject(message, LiWuBoFangBean.class);
                XiaZaiLiWuBean liWuBean = xiaZaiLiWuBeanBox.get(Long.parseLong(parseUser.getLiwuID()));
                if (liWuBean.isJY()) {//解压过了
                    linkedBlockingQueueLW.offer(parseUser.getLiwuID());
                    //  playDongHua(parseUser.getLiwuID());
                }
                synchronized (BoFangActivity.this) {
                    boFangBeanList.add(0, parseUser);
                    liWuBoFangAdapter.notifyDataSetChanged();
                    linkedBlockingQueue.offer(1);
                }
                link_userinfo(idid + "");
                break;
            case "rufang": //收到观众入房消息
                YongHuListBean yongHuListBean = com.alibaba.fastjson.JSONObject.parseObject(message, YongHuListBean.class);
                MyApplication.myApplication.getYongHuListBeanBox().put(yongHuListBean);
                List<YongHuListBean> listBeans = yongHuListBeanBox.query().orderDesc(YongHuListBean_.jingbi).build().find(0, 8);
                guanZhongBeanList.clear();
                guanZhongBeanList.addAll(listBeans);
                guanZhongAdapter.notifyDataSetChanged();

                LiaoTianBean bean = new LiaoTianBean();
                bean.setNickname(yongHuListBean.getName());
                bean.setType(2);
                bean.setNeirong("加入直播间");
                bean.setHeadImage(yongHuListBean.getHeadImage());
                bean.setUserid((yongHuListBean.getId()));
                lingshiList.add(bean);
                numberGZ += 1;
                guanzhongxiangqiang.setText(numberGZ + "");
                break;
            case "tuifang": //收到观众tui房消息
                YongHuListBean huListBean = com.alibaba.fastjson.JSONObject.parseObject(message, YongHuListBean.class);
                MyApplication.myApplication.getYongHuListBeanBox().remove(huListBean.getId());
                guanZhongAdapter.notifyDataSetChanged();
                numberGZ -= 1;
                if (numberGZ < 0) {
                    numberGZ = 0;
                }
                guanzhongxiangqiang.setText(numberGZ + "");
                break;
            case "updatapkall": //主播更新pk
                // Log.d("BoFangActivity", "主播更新pk1");
                String[] sss = message.split(",");
                gengxingPK(sss[0], sss[1]);
                break;
            case "startpkall": //主播开始pk
                if (timer1 != null)
                    timer1.cancel();
                timer1 = null;
                if (timer2 != null)
                    timer2.cancel();
                timer2 = null;
                startPK();
                break;
            case "zhongtupkall": //中途加入pk
                // Log.d("BoFangActivity", "zhongtu加入1");
                if (timer1 != null || timer2 != null) {//因为其他人的进入也会收到这个广播，所以如果不为空就表示已经收到过了
                    return;
                }
                String[] zzz = message.split(",");
                //  Log.d("BoFangActivity", "zhongtu加入"+zzz);
                zhongtuPK(Long.parseLong(zzz[1]), zzz[0]);
                //    Log.d("BoFangActivity", "zhongtu加入2");
                break;
            case "roomNum": //当前房间人数
                numberGZ = Long.parseLong(message);
                guanzhongxiangqiang.setText(numberGZ + "");
                break;
            case "setGLY": //设置或取消管理员
                if (message.equals("0")) {
                    isGLY = false;
                } else {
                    isGLY = true;
                }
                break;
            case "dangqianGZ"://当前观众
                List<YongHuListBean> yongHuListBean1 = com.alibaba.fastjson.JSONObject.parseArray(message, YongHuListBean.class);
                // Log.d("BoFangActivity", "yongHuListBean1.size():" + yongHuListBean1.size());
                for (YongHuListBean listBean : yongHuListBean1) {
                    MyApplication.myApplication.getYongHuListBeanBox().put(listBean);
                }
                List<YongHuListBean> listBeans2 = yongHuListBeanBox.query().orderDesc(YongHuListBean_.jingbi).build().find(0, 8);
                guanZhongBeanList.clear();
                guanZhongBeanList.addAll(listBeans2);
                guanZhongAdapter.notifyDataSetChanged();
                break;
            case "guanbiPK"://关掉PK
                if (timer1 != null)
                    timer1.cancel();
                timer1 = null;
                if (timer2 != null)
                    timer2.cancel();
                timer2 = null;
                chengfa.setVisibility(View.GONE);
                pkjgim1.setVisibility(View.GONE);
                pkjgim2.setVisibility(View.GONE);
                group.setVisibility(View.GONE);
                toptop.setVisibility(View.GONE);
                //全屏播放
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txCloudVideoView.getLayoutParams();
                params.height = heightPixels;
                txCloudVideoView.setLayoutParams(params);
                txCloudVideoView.invalidate();
                break;
            case "chaxunJY":
                link_chaxunJY(idid + "", baoCunBean.getUserid() + "");
                break;
            case "guanzhu":
                LiaoTianBean bean22 = new LiaoTianBean();
                bean22.setNickname(message);
                bean22.setType(2);
                bean22.setNeirong("关注了主播");
                //bean.setHeadImage(yongHuListBean.getHeadImage());
                bean22.setUserid(0);
                lingshiList.add(bean22);

                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxMSG(MsgWarp msgWarp) {
        if (msgWarp.getType() == 1005) {//收到输入的消息广播
            //ToastUtils.showInfo(BoFangActivity.this,"收到关闭键盘通知");
            if (!msgWarp.getMsg().equals("")) {
                if (!isJY) {
                    try {
//                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null && txCloudVideoView != null) {
                            // imm.hideSoftInputFromWindow(txCloudVideoView.getWindowToken(), 0);
                            imm.showSoftInput(txCloudVideoView, InputMethodManager.SHOW_IMPLICIT);
                        }
                        if (popupwindow != null)
                            popupwindow.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showInfo(BoFangActivity.this, "收到关闭键盘通知" + e.getMessage());
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

                } else {
                    ToastUtils.showError(BoFangActivity.this, "你已被禁言!");
                }
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
                liWuBoFangBean.setNum(Integer.valueOf(msgWarp.getNum()));//数量
                XiaZaiLiWuBean nliwuname = MyApplication.myApplication.getXiaZaiLiWuBeanBox().get(Integer.parseInt(msgWarp.getMsg()));
                if (nliwuname == null) {
                    ToastUtils.showError(BoFangActivity.this, "未找到本地礼物");
                    return;
                }
                liWuBoFangBean.setLiwuID(msgWarp.getMsg());
                liWuBoFangBean.setLiwuName(nliwuname.getGiftName());
                liWuBoFangBean.setLiwuPath(nliwuname.getGiftUrl());
                String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(liWuBoFangBean);
                mlvbLiveRoom.sendRoomCustomMsg("liwudonghua2", jsonString, new SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        Log.d("BoFangActivity", "发送礼物自定义消息失败" + errInfo + errCode);
                        ToastUtils.showError(BoFangActivity.this, "赠送礼物失败");
                    }
                    @Override
                    public void onSuccess() {
                        Log.d("BoFangActivity", "发送礼物自定义消息成功");
                        synchronized (BoFangActivity.this) {
                            boFangBeanList.add(0, liWuBoFangBean);
                            liWuBoFangAdapter.notifyDataSetChanged();
                            linkedBlockingQueue.offer(1);
                        }
                        link_userinfo(idid + "");
                    }
                });

            } else { //礼物类型2
                LiWuBoFangBean liWuBoFangBean = new LiWuBoFangBean();
                liWuBoFangBean.setHeadImage(baoCunBean.getHeadImage());
                liWuBoFangBean.setId(baoCunBean.getUserid());
                liWuBoFangBean.setName(baoCunBean.getNickname());
                liWuBoFangBean.setNum(Integer.valueOf(msgWarp.getNum()));//数量
                XiaZaiLiWuBean nliwuname = MyApplication.myApplication.getXiaZaiLiWuBeanBox().get(Integer.parseInt(msgWarp.getMsg()));
                if (nliwuname == null) {
                    ToastUtils.showError(BoFangActivity.this, "未找到本地礼物");
                    return;
                }
                liWuBoFangBean.setLiwuID(msgWarp.getMsg());
                liWuBoFangBean.setLiwuName(nliwuname.getGiftName());
                liWuBoFangBean.setLiwuPath(nliwuname.getGiftUrl());
                String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(liWuBoFangBean);
                mlvbLiveRoom.sendRoomCustomMsg("liwudonghua2", jsonString, new SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        //  Log.d("BoFangActivity", "发送礼物自定义消息失败" + errInfo + errCode);
                        ToastUtils.showError(BoFangActivity.this, "赠送礼物失败");
                        // playDongHua(msgWarp.getMsg());
                    }
                    @Override
                    public void onSuccess() {
                        Log.d("BoFangActivity", "发送礼物自定义消息成功2");
                        //播放.query().equal(Subject_.teZhengMa, new String(result.faceToken)).build()
                        XiaZaiLiWuBean liWuBean = xiaZaiLiWuBeanBox.get(Long.parseLong(msgWarp.getMsg()));
                        // Log.d("BoFangActivity", "liWuBean.isJY():" + liWuBean.isJY());
                        if (liWuBean.isJY()) {//解压过了
                            linkedBlockingQueueLW.offer(msgWarp.getMsg());
                        }
                        synchronized (BoFangActivity.this) {
                            boFangBeanList.add(0, liWuBoFangBean);
                            liWuBoFangAdapter.notifyDataSetChanged();
                            linkedBlockingQueue.offer(1);
                        }
                        link_userinfo(idid + "");

                    }
                });
            }
        }
        if (msgWarp.getType() == 3369) {//发送设置管理员自定义消息1是禁言 0是未禁言
            mlvbLiveRoom.sendRoomCustomMsg("chaxunJY", "0", new SendRoomCustomMsgCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                }

                @Override
                public void onSuccess() {
                }
            });
        }

        if (msgWarp.getType() == 887700) {//@别人
            Log.d("ZhiBoActivity", "收到887700" + msgWarp.getMsg());
            InputMethodUtils.showOrHide(this, txCloudVideoView);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(200);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    });
                    SystemClock.sleep(200);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (popupwindow != null)
                                popupwindow.dismiss();
                            popupwindow = new InputPopupwindow(BoFangActivity.this, msgWarp.getMsg());
                            popupwindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, jianpangHight);
                        }
                    });
                }
            }).start();
        }
        if (msgWarp.getType() == 100868) {
            //横幅
            String[] sls = msgWarp.getMsg().split(",");
            View view_dk = View.inflate(BoFangActivity.this, R.layout.hengfu_item, null);
            TextView tv1 = view_dk.findViewById(R.id.name_3);
            TextView tv2 = view_dk.findViewById(R.id.name_zhibojian);
            TextView tv3 = view_dk.findViewById(R.id.name_liwu);
            tv1.setText(sls[0]);
            tv2.setText(sls[1]);
            tv3.setText(sls[2]);
            rootv.addView(view_dk);
            view_dk.setX(width);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view_dk.getLayoutParams();
            //params.leftMargin = width;
            view_dk.setLayoutParams(params);
            view_dk.invalidate();
            ValueAnimator animator = ValueAnimator.ofInt(width,-width);
            Interpolator interpolator = new LinearInterpolator();
            animator.setInterpolator(interpolator);
            //如下传入多个参数，效果则为0->5,5->3,3->10
            //ValueAnimator animator = ValueAnimator.ofInt(0,5,3,10);
            //设置动画的基础属性
            animator.setDuration(8000);//播放时长
           // animator.setStartDelay(300);//延迟播放
            animator.setRepeatCount(0);//重放次数
          //  animator.setRepeatMode(ValueAnimator.RESTART);
            //重放模式
            //ValueAnimator.START：正序
            //ValueAnimator.REVERSE：倒序
            //设置更新监听
            //值 改变一次，该方法就执行一次
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //获取改变后的值
                    int currentValue = (int) animation.getAnimatedValue();
                    //输出改变后的值
                  //  Log.d("1111", "onAnimationUpdate: " + currentValue);
                    //改变后的值发赋值给对象的属性值
                    view_dk.setTranslationX(currentValue);
                    //刷新视图
                    view_dk.requestLayout();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d("BoFangActivity", "结束");
                    super.onAnimationEnd(animation);
                    rootv.removeView(view_dk);
                }
            });
            //启动动画
            animator.start();

        }

    }

    private void playDongHua(String idid) {
        Log.d("BoFangActivity", "播放礼物id:" + idid);
        animationView.setVisibility(View.VISIBLE);
        final String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //提供一个代理接口从 SD 卡读取 images 下的图片
        animationView.cancelAnimation();
        animationView.removeAllAnimatorListeners();
        animationView.setImageAssetDelegate(new ImageAssetDelegate() {
            @Override
            public Bitmap fetchBitmap(LottieImageAsset asset) {
                //  Log.d("BoFangActivity", asset.getFileName());
                Bitmap bitmap = null;
                FileInputStream fileInputStream = null;
                try {
                    String paths = absolutePath + File.separator + "lanjing/" + idid + "/images/" + asset.getFileName();
                    File file=new File(paths);
                    if (file.exists()){
                        fileInputStream = new FileInputStream( paths);
                        bitmap = BitmapFactory.decodeStream(fileInputStream);
                    }else {
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.GONE);
                    XiaZaiLiWuBean xiaZaiLiWuBean = xiaZaiLiWuBeanBox.get(Long.parseLong(idid));
                    if (xiaZaiLiWuBean != null) {
                        xiaZaiLiWuBean.setD(false);
                        xiaZaiLiWuBean.setJY(false);
                        xiaZaiLiWuBeanBox.put(xiaZaiLiWuBean);
                    }
                    isLWPlay = true;
                } finally {
                    try {
                        if (bitmap == null) {
                            Log.d("BoFangActivity", "礼物bitmap==null");
                            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
                        }
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                    } catch (IOException e) {
                        Log.d(TAG, "e:" + e);
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);
                        isLWPlay = true;
                    }
                }
                return bitmap;
            }
        });
        String sss = ReadAssetsJsonUtil.readJSON(idid);
        if (sss.equals("")){
            animationView.setVisibility(View.GONE);
            return;
        }
        animationView.setAnimationFromJson(sss);
        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("BoFangActivity", "结束了");
                //  animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
                isLWPlay = true;
            }
        });
        animationView.playAnimation();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //查询主播信息
        link_userinfo(idid + "");

    }


    @Override
    protected void onDestroy() {
        timer.cancel();
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
        if (timer1 != null)
            timer1.cancel();
        timer1 = null;
        if (timer2 != null)
            timer2.cancel();
        timer2 = null;

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
        tuiChuDialog.setTextView("确定要退出直播间?");
        tuiChuDialog.setOnQueRenListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //退出时发送退房自定义通知

                mlvbLiveRoom.exitRoom(new ExitRoomCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        mlvbLiveRoom.setListener(null);
                        tuiChuDialog.dismiss();
                        BoFangActivity.this.finish();
                    }

                    @Override
                    public void onSuccess() {
                        mlvbLiveRoom.setListener(null);
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

    @OnClick({R.id.guanzhongxiangqiang, R.id.paihangView, R.id.tuichu, R.id.fenxiang, R.id.touxiang,
            R.id.fanzhuang, R.id.meiyan, R.id.pk, R.id.shuodian, R.id.video_player, R.id.guanzhu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guanzhongxiangqiang:
                YongHuListDialog yongHuListDialog = new YongHuListDialog(idid + "");
                yongHuListDialog.show(getSupportFragmentManager(), "yuanogn");
                break;
            case R.id.paihangView:
                PaiHangListDialog paiHangListDialog = new PaiHangListDialog();
                paiHangListDialog.show(getSupportFragmentManager(), "paihang");
                break;
            case R.id.tuichu:
                TuiChuDialog tuiChuDialog = new TuiChuDialog(BoFangActivity.this);
                tuiChuDialog.setTextView("确定要退出直播间?");
                tuiChuDialog.setOnQueRenListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //退出时发送退房自定义通知
                        mlvbLiveRoom.exitRoom(new ExitRoomCallback() {
                            @Override
                            public void onError(int errCode, String errInfo) {
                                mlvbLiveRoom.setListener(null);
                                tuiChuDialog.dismiss();
                                BoFangActivity.this.finish();
                            }

                            @Override
                            public void onSuccess() {
                                mlvbLiveRoom.setListener(null);
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
                // mlvbLiveRoom.switchCamera();
                break;
            case R.id.meiyan:
                //美颜是转盘
                ZhuanPanDialog zhuanPanDialog = new ZhuanPanDialog();
                zhuanPanDialog.show(getSupportFragmentManager(), "zhuanpan");
                break;
            case R.id.pk:
                //pk是礼物
                LiWuDialog liWuDialog = new LiWuDialog(idid + "", name.getText().toString());
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
                                popupwindow = new InputPopupwindow(BoFangActivity.this, "");
                                popupwindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, jianpangHight);
                            }
                        });
                    }
                }).start();
                break;
            case R.id.guanzhu:
                if (isGuanzhu) {
                    link_quxiaoguanzhu(idid + "");
                } else {
                    link_guanzhu(idid + "");
                }
                break;
            case R.id.touxiang:
                ZhuBoXinxiDialog zhuBoXinxiDialog = new ZhuBoXinxiDialog(idid + "");
                zhuBoXinxiDialog.show(getSupportFragmentManager(), "zhuboxinxi");
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

    private void link_guanzhu(String id) {
        // MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = null;
        body = new FormBody.Builder()
                .add("id", id)
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
                .url(Consts.URL + "/user/subscribe/" + id);
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
                    Log.d("AllConnects", "关注:" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
//                    Gson gson = new Gson();
//                    LogingBe logingBe = gson.fromJson(jsonObject, LogingBe.class);
                    if (jsonObject.get("code").getAsInt() == 1) {
                        ToastUtils.showInfo(BoFangActivity.this, "关注成功");
                    } else {
                        ToastUtils.showInfo(BoFangActivity.this, jsonObject.get("desc").getAsString());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isGuanzhu = true;
                            guanzhu.setBackgroundResource(R.drawable.guanzhu2);
                            LiaoTianBean bean = new LiaoTianBean();
                            bean.setNickname(baoCunBean.getNickname());
                            bean.setType(2);
                            bean.setNeirong("关注了主播");
                            //bean.setHeadImage(yongHuListBean.getHeadImage());
                            bean.setUserid(0);
                            lingshiList.add(bean);

                            mlvbLiveRoom.sendRoomCustomMsg("guanzhu", baoCunBean.getNickname(), new SendRoomCustomMsgCallback() {
                                @Override
                                public void onError(int errCode, String errInfo) {
                                    //  ToastUtils.showError(BoFangActivity.this, "发送进房消息失败,请退出后重试");
                                }

                                @Override
                                public void onSuccess() {

                                }
                            });

                        }
                    });
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    // ToastUtils.showError(BoFangActivity.this, "获取数据失败");

                }
            }
        });
    }


    private void link_quxiaoguanzhu(String id) {
        // MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = null;
        body = new FormBody.Builder()
                .add("id", id)
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
                .url(Consts.URL + "/user/unsubscribe/" + id);
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
                    Log.d("AllConnects", "关注:" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
//                    Gson gson = new Gson();
//                    LogingBe logingBe = gson.fromJson(jsonObject, LogingBe.class);
                    if (jsonObject.get("code").getAsInt() == 1) {
                        ToastUtils.showInfo(BoFangActivity.this, "已取消关注");
                    } else {
                        ToastUtils.showInfo(BoFangActivity.this, jsonObject.get("desc").getAsString());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isGuanzhu = false;
                            guanzhu.setBackgroundResource(R.drawable.yiguanzhu);
                        }
                    });
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    // ToastUtils.showError(BoFangActivity.this, "获取数据失败");

                }
            }
        });
    }

    private void link_isguanzhu(String id) {
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + MyApplication.myApplication.getBaoCunBean().getSession())
                .get()
                .url(Consts.URL + "/user/focus/" + id);
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
                    Log.d("AllConnects", "是否关注:" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    // Gson gson = new Gson();
                    if (jsonObject.get("desc").getAsString().equals("已关注")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                guanzhu.setBackgroundResource(R.drawable.guanzhu2);
                                isGuanzhu = true;
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                guanzhu.setBackgroundResource(R.drawable.yiguanzhu);
                                isGuanzhu = false;
                            }
                        });
                    }


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
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + MyApplication.myApplication.getBaoCunBean().getSession())
                .get()
                .url(Consts.URL + "/user/info/" + idid + "/" + baoCunBean.getUserid());
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
                    Log.d("AllConnects", "花了多少钱:" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    PuTongInfio logingBe = gson.fromJson(jsonObject, PuTongInfio.class);
                    if (logingBe.getCode() == 2000) {
                        YongHuListBean yongHuListBean = new YongHuListBean();
                        yongHuListBean.setName(baoCunBean.getNickname());
                        yongHuListBean.setDengji(baoCunBean.getAnchorLevel());
                        yongHuListBean.setHeadImage(baoCunBean.getHeadImage());
                        yongHuListBean.setId(baoCunBean.getUserid());
                        yongHuListBean.setJingbi(logingBe.getResult().getTotal());
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
                    SystemClock.sleep(5000);
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
            Log.d("RecognizeThread", "中断了弹窗线程");
        }

        @Override
        public void interrupt() {
            isRing = true;
            Log.d("RecognizeThread", "中断了弹窗线程");
            super.interrupt();
        }
    }

    private boolean isLWPlay = false;

    private class TanChuangThreadLW extends Thread {
        boolean isRing;

        @Override
        public void run() {
            while (!isRing) {
                try {
                    //有动画 ，延迟到一秒一次
                    String id = linkedBlockingQueueLW.take();
                    isLWPlay = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playDongHua(id);
                        }
                    });
                    while (!isLWPlay) {
                        SystemClock.sleep(200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d("RecognizeThread", "中断了弹窗线程");
        }

        @Override
        public void interrupt() {
            isRing = true;
            Log.d("RecognizeThread", "中断了弹窗线程");
            super.interrupt();
        }
    }

    private void link_userinfo(String id) {
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + MyApplication.myApplication.getBaoCunBean().getSession())
                .get()
                .url(Consts.URL + "/anchor/info/" + id);
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
                    if (logingBe.getCode() == 2000) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                paiming.setText("总排名:" + logingBe.getResult().getRank());
                                name.setText(logingBe.getResult().getNickname());
                                fangjianhao.setText("LANJING " + logingBe.getResult().getId());
                                double xg = logingBe.getResult().getStarLight();
                                if (xg >= 10000) {

                                    xingguang.setText(Utils.doubleToString(xg / 10000.0) + "万");
                                } else {
                                    xingguang.setText(xg + "");
                                }
                                Glide.with(BoFangActivity.this)
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


    private void startPK() {
        pktv1.setText("0");
        pktv2.setText("0");
        group.setVisibility(View.VISIBLE);
        toptop.setVisibility(View.VISIBLE);
        //改变视频大小
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txCloudVideoView.getLayoutParams();
        params.height = (int) (hight * 0.38);
        txCloudVideoView.setLayoutParams(params);
        txCloudVideoView.invalidate();
        timer1 = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String st = Utils.timeParse((millisUntilFinished));
                daojishi.setText(st);
            }

            @Override
            public void onFinish() {
                //第一次倒计时结束
                chengfa.setVisibility(View.VISIBLE);
                pkjgim1.setVisibility(View.VISIBLE);
                pkjgim2.setVisibility(View.VISIBLE);
                int p1 = Integer.parseInt(pktv1.getText().toString());
                int p2 = Integer.parseInt(pktv2.getText().toString());
                if (p1 > p2) {
                    pkjgim1.setBackgroundResource(R.drawable.shenlibg);
                    pkjgim2.setBackgroundResource(R.drawable.shibaibg);
                } else if (p1 < p2) {
                    pkjgim1.setBackgroundResource(R.drawable.shibaibg);
                    pkjgim2.setBackgroundResource(R.drawable.shenlibg);
                } else {
                    pkjgim1.setBackgroundResource(R.drawable.pingjubg);
                    pkjgim2.setBackgroundResource(R.drawable.pingjubg);
                }
                timer2 = new CountDownTimer(100000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String st = Utils.timeParse((millisUntilFinished));
                        daojishi.setText(st);
                    }

                    @Override
                    public void onFinish() {
                        //惩罚倒计时结束
                        chengfa.setVisibility(View.GONE);
                        pkjgim1.setVisibility(View.GONE);
                        pkjgim2.setVisibility(View.GONE);
                        group.setVisibility(View.GONE);
                        //全屏播放
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txCloudVideoView.getLayoutParams();
                        params.height = heightPixels;
                        txCloudVideoView.setLayoutParams(params);
                        txCloudVideoView.invalidate();
                        toptop.setVisibility(View.GONE);
                        timer1 = null;
                        timer2 = null;
                    }
                };
                timer2.start();
            }
        };
        timer1.start();
    }

    private void zhongtuPK(long time1, String type) {
        pktv1.setText("0");
        pktv2.setText("0");
        group.setVisibility(View.VISIBLE);
        toptop.setVisibility(View.VISIBLE);
        //改变视频大小
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txCloudVideoView.getLayoutParams();
        params.height = (int) (hight * 0.38);
        txCloudVideoView.setLayoutParams(params);
        txCloudVideoView.invalidate();
        if (type.equals("1")) {//第一阶段
            timer1 = new CountDownTimer(time1, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String st = Utils.timeParse((millisUntilFinished));
                    daojishi.setText(st);
                }

                @Override
                public void onFinish() {
                    //第一次倒计时结束
                    chengfa.setVisibility(View.VISIBLE);
                    pkjgim1.setVisibility(View.VISIBLE);
                    pkjgim2.setVisibility(View.VISIBLE);
                    int p1 = Integer.parseInt(pktv1.getText().toString());
                    int p2 = Integer.parseInt(pktv2.getText().toString());
                    if (p1 > p2) {
                        pkjgim1.setBackgroundResource(R.drawable.shenlibg);
                        pkjgim2.setBackgroundResource(R.drawable.shibaibg);
                    } else if (p1 < p2) {
                        pkjgim1.setBackgroundResource(R.drawable.shibaibg);
                        pkjgim2.setBackgroundResource(R.drawable.shenlibg);
                    } else {
                        pkjgim1.setBackgroundResource(R.drawable.pingjubg);
                        pkjgim2.setBackgroundResource(R.drawable.pingjubg);
                    }
                    timer2 = new CountDownTimer(100000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String st = Utils.timeParse((millisUntilFinished));
                            daojishi.setText(st);
                        }

                        @Override
                        public void onFinish() {
                            //惩罚倒计时结束
                            chengfa.setVisibility(View.GONE);
                            pkjgim1.setVisibility(View.GONE);
                            pkjgim2.setVisibility(View.GONE);
                            group.setVisibility(View.GONE);
                            //全屏播放
                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txCloudVideoView.getLayoutParams();
                            params.height = heightPixels;
                            txCloudVideoView.setLayoutParams(params);
                            txCloudVideoView.invalidate();
                            toptop.setVisibility(View.GONE);
                            timer1 = null;
                            timer2 = null;
                        }
                    };
                    timer2.start();

                }
            };
            timer1.start();
        } else {

            chengfa.setVisibility(View.VISIBLE);
            timer2 = new CountDownTimer(time1, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String st = Utils.timeParse((millisUntilFinished));
                    daojishi.setText(st);
                    pkjgim1.setVisibility(View.VISIBLE);
                    pkjgim2.setVisibility(View.VISIBLE);
                    int p1 = Integer.parseInt(pktv1.getText().toString());
                    int p2 = Integer.parseInt(pktv2.getText().toString());
                    if (p1 > p2) {
                        pkjgim1.setBackgroundResource(R.drawable.shenlibg);
                        pkjgim2.setBackgroundResource(R.drawable.shibaibg);
                    } else if (p1 < p2) {
                        pkjgim1.setBackgroundResource(R.drawable.shibaibg);
                        pkjgim2.setBackgroundResource(R.drawable.shenlibg);
                    } else {
                        pkjgim1.setBackgroundResource(R.drawable.pingjubg);
                        pkjgim2.setBackgroundResource(R.drawable.pingjubg);
                    }
                }

                @Override
                public void onFinish() {
                    //惩罚倒计时结束
                    chengfa.setVisibility(View.GONE);
                    pkjgim1.setVisibility(View.GONE);
                    pkjgim2.setVisibility(View.GONE);
                    group.setVisibility(View.GONE);
                    //全屏播放
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) txCloudVideoView.getLayoutParams();
                    params.height = heightPixels;
                    txCloudVideoView.setLayoutParams(params);
                    txCloudVideoView.invalidate();
                    toptop.setVisibility(View.GONE);
                    timer2 = null;
                }
            };
            timer2.start();
        }
    }

    private void gengxingPK(String me1, String to1) {
        int pkv1 = pkview1.getWidth();
        int pkv2 = pkview2.getWidth();
        //(A-B)÷Bx100%

        float me = Float.parseFloat(me1);
        float to = Float.parseFloat(to1);
        if (me == 0 && to == 0) {
            return;
        }
        pktv1.setText(((int) me) + "");
        pktv2.setText(((int) to) + "");
        if (pkv1 == 0 || pkv2 == 0) {
            return;
        } else {
            if (isON) {
                isON = false;
                pktWidth = pkv1;
            }
            if (me > to) {
                float f1 = (float) Math.abs(((to - me) / me) * (pktWidth / 2.0));
                pkxuetiao((int) (pktWidth + f1), pkview1);
                pkxuetiao((int) (pktWidth - f1), pkview2);

            } else if (me < to) {
                float f2 = (float) Math.abs((((me - to) / to) * (pktWidth / 2.0)));
                pkxuetiao((int) (pktWidth - f2), pkview1);
                pkxuetiao((int) (pktWidth + f2), pkview2);

            }
        }
        Log.d("ZhiBoActivity", "dddd");
    }

    private void pkxuetiao(int width1, View view) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = width1;
        view.setLayoutParams(params);
        view.invalidate();
    }

    private void link_chaxunJY(String zhuboid, String id) {
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + MyApplication.myApplication.getBaoCunBean().getSession())
                .get()
                .url(Consts.URL + "/im/ban/" + id + "?group=" + zhuboid);
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
                    Log.d("AllConnects", "查询是否禁言:" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    if (jsonObject.get("code").getAsInt() == 1) {//禁言了
                        isJY = true;
                    }
                    if (jsonObject.get("code").getAsInt() == 0) {//禁言了
                        isJY = false;
                    }
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    // ToastUtils.showError(BoFangActivity.this, "获取数据失败");

                }
            }
        });
    }


}
