package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public interface IModelGoods {
    void loadNewGoodsMode(Context context, int catId, int pageId, int PageSize, OnCompleteListener<NewGoodsBean[]> listener);

    void loadBoutiqueMode(Context context, OnCompleteListener<BoutiqueBean[]> listener);

    void loadGoodsDetailMode(Context context, String goodsId, OnCompleteListener<GoodsDetailsBean> listener);

    void loadCategoryGroup(Context context, OnCompleteListener<CategoryGroupBean[]> listener);

    void loadCategoryChild(Context context, int parentId, OnCompleteListener<CategoryChildBean[]> listener);

    void loadCategoryGoodsDetail(Context context, int cat_id, int pageId, int PageSize, OnCompleteListener<NewGoodsBean[]> listener);

}
