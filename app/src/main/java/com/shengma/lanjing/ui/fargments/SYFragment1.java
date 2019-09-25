package com.shengma.lanjing.ui.fargments;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shengma.lanjing.MyApplication;
import com.shengma.lanjing.R;
import com.shengma.lanjing.adapters.GuanZhuAdapter;
import com.shengma.lanjing.beans.GuanZhuBean;
import com.shengma.lanjing.ui.zhibo.BoFangActivity;
import com.shengma.lanjing.utils.Consts;
import com.shengma.lanjing.utils.DisplayUtils;
import com.shengma.lanjing.utils.GsonUtil;
import com.shengma.lanjing.utils.ToastUtils;
import com.shengma.lanjing.views.GridDividerItemDecoration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class SYFragment1 extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int pag=1;
    private List<GuanZhuBean.ResultBean> beanList=new ArrayList<>();
    private GuanZhuAdapter adapter;
    private LinearLayout rrr;

    public SYFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_syfragment1, container, false);
        swipeRefreshLayout=view.findViewById(R.id.refreshLayout);
        rrr= view.findViewById(R.id.rrr);
        recyclerView=view.findViewById(R.id.recyclerview);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#3EE1F7"), Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                link_list();
            }
        });


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        adapter=new GuanZhuAdapter(beanList);
        recyclerView.setAdapter(adapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        int firstAndLastColumnW = DisplayUtils.dp2px(getActivity(), 16);
        int firstRowTopMargin = DisplayUtils.dp2px(getActivity(), 16);
        GridDividerItemDecoration gridDividerItemDecoration =
                new GridDividerItemDecoration(getActivity(), firstAndLastColumnW, firstRowTopMargin, firstRowTopMargin);
        gridDividerItemDecoration.setFirstRowTopMargin(firstRowTopMargin);
        gridDividerItemDecoration.setLastRowBottomMargin(firstRowTopMargin);
        recyclerView.addItemDecoration(gridDividerItemDecoration);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "position:" + position);
                if (MyApplication.myApplication.getBaoCunBean().isLiwuISOK()){
                    Intent intent=new Intent(getActivity(), BoFangActivity.class);
                    intent.putExtra("idid",beanList.get(position).getId());
                    startActivity(intent);
                }else {
                    if (getActivity()!=null)
                    ToastUtils.showInfo(getActivity(),"抱歉,礼物资源未下载完成");
                }

            }
        });

        link_list();
        return view;
    }


    private void link_list() {
        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .header("Cookie","JSESSIONID="+ MyApplication.myApplication.getBaoCunBean().getSession())
                .get()
                .url(Consts.URL+"/live/list/focus?page="+pag+"&pageSize=10");

        // step 3：创建 Call 对象
        Call call = MyApplication.myApplication.getOkHttpClient().newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                if (getActivity()!=null)
                ToastUtils.showError(getActivity(),"获取数据失败,请检查网络");
                if (getActivity()!=null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                if (getActivity()!=null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("LogingActivity", "关注列表"+ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    GuanZhuBean bean = gson.fromJson(jsonObject, GuanZhuBean.class);
                    if (bean.getCode()==2000) {
                        if (getActivity()!=null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (bean.getResult().size()>0){
                                        pag+=1;
                                    }
                                    beanList.addAll(bean.getResult());
                                    adapter.notifyDataSetChanged();
                                    if (beanList.size()==0){
                                        rrr.setVisibility(View.VISIBLE);
                                    }else {
                                        rrr.setVisibility(View.GONE);
                                    }
                                }
                            });
                    }
                } catch (Exception e) {
                    Log.d("AllConnects", e.getMessage() + "异常");
                    if (getActivity()!=null)
                    ToastUtils.showError(getActivity(),"获取数据失败");
                    if (getActivity()!=null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                }
            }
        });
    }


}
