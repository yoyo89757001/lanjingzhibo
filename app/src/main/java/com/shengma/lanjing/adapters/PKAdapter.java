package com.shengma.lanjing.adapters;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shengma.lanjing.R;
import com.shengma.lanjing.beans.MsgWarp;
import com.shengma.lanjing.beans.ZaiXianZhuBo;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PKAdapter extends BaseQuickAdapter<ZaiXianZhuBo.ResultBean,BaseViewHolder> {



    public PKAdapter(List<ZaiXianZhuBo.ResultBean> list) {
        super(R.layout.pk_item, list);
    }


    @Override
    protected void convert(BaseViewHolder helper, ZaiXianZhuBo.ResultBean item) {
        helper.setText(R.id.name, item.getNickname());
        helper.setText(R.id.dengji, "Lv"+item.getLevel());
        if (item.getSex()==1){
            helper.setImageResource(R.id.xingbie,R.drawable.nan);
        }else {
            helper.setImageResource(R.id.xingbie,R.drawable.nv);
        }
        Glide.with(mContext)
                .load(item.getHeadImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into((ImageView) helper.getView(R.id.touxiang));
         Button view= helper.getView(R.id.pk);
         view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Log.d(TAG, "roomCreator:"+item.getId());
                 EventBus.getDefault().post(new MsgWarp(1006,item.getId()+"",item.getPlaySafeUrl()));
             }
         });
        //RequestOptions.bitmapTransform(new CircleCrop())//圆形
        //RequestOptions.bitmapTransform(new RoundedCorners( 5))//圆角
    }



}
