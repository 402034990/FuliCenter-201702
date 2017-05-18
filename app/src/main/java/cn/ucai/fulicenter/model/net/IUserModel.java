package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public interface IUserModel {
    void login(Context context,String userName, String passWord, OnCompleteListener<String> listener);

    void register(Context context,String userName, String Nick, String pssWord,OnCompleteListener<String> listener);

    void updateNick(Context context, String userName, String Nick, OnCompleteListener<String> listener);

    void updateAvatar(Context context, String hxIdName, File mFile, OnCompleteListener<String> listener);

    void findCarts(Context context, String userName, OnCompleteListener<CartBean[]> listener);

    void addCarts(Context context, int goods_id, String userName, int count, boolean isChecked, OnCompleteListener<MessageBean> listener);

    void deleteCart(Context context, int id, OnCompleteListener<MessageBean> listener);

    void updateCart(Context context, int id, int count, boolean isChecked, OnCompleteListener<MessageBean> listener);

    void findCollects(Context context,String userName, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener);

    void isCollect(Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener);

    void addCollect(Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener);

    void deleteCollect(Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener);

    void findCollectsCount(Context context, String userName, OnCompleteListener<MessageBean> listener);

}
