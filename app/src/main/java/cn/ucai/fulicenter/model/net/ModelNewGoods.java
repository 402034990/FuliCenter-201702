package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class ModelNewGoods implements IModelGoods {

    @Override
    public void loadNewGoodsMode(Context context, int catId, int pageId, int pageSize, OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,""+catId)
                .addParam(I.PAGE_ID,""+pageId)
                .addParam(I.PAGE_SIZE,pageSize+"")
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
}
