package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public interface IModelGoods {
    void loadNewGoodsMode(Context context, int catId, int pageId, int PageSize, OnCompleteListener<NewGoodsBean[]> listener);
}
