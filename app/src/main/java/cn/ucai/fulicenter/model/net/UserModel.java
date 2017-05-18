package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class UserModel implements IUserModel{

    @Override
    public void login(Context context, String userName, String passWord, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME, userName)
                .addParam(I.User.PASSWORD, passWord)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void register(Context context, String userName, String Nick, String passWord, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,Nick)
                .addParam(I.User.PASSWORD,passWord)
                .post()
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void updateNick(Context context, String userName, String Nick, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,Nick)
                .targetClass(String.class)
                .execute(listener);

    }

    @Override
    public void updateAvatar(Context context, String hxIdName, File mFile, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID, hxIdName)
                .addParam(I.AVATAR_TYPE, I.AVATAR_TYPE_USER_PATH)
                .addFile2(mFile)
                .post()
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void findCarts(Context context, String userName, OnCompleteListener<CartBean[]> listener) {
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,userName)
                .targetClass(CartBean[].class)
                .execute(listener);
    }

    @Override
    public void addCarts(Context context, int goods_id, String userName, int count, boolean isChecked, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Goods.KEY_GOODS_ID,goods_id+"")
                .addParam(I.Cart.USER_NAME,userName)
                .addParam(I.Cart.COUNT,count+"")
                .addParam(I.Cart.IS_CHECKED,String.valueOf(isChecked))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void deleteCart(Context context, int id, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID,String.valueOf(id))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void updateCart(Context context, int id, int count, boolean isChecked, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,String.valueOf(id))
                .addParam(I.Cart.COUNT,String.valueOf(count))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(isChecked))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void findCollects(Context context, String userName,int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener) {
        OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME,userName)
                .addParam(I.PAGE_ID,pageId+"")
                .addParam(I.PAGE_SIZE,pageSize+"")
                .targetClass(CollectBean[].class)
                .execute(listener);
    }

    @Override
    public void isCollect(Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.GOODS_ID,goodsId+"")
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void addCollect(Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener) {
        collect(I.REQUEST_ADD_COLLECT,context, goodsId, userName, listener);
    }

    private void collect(String action,Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(action)
                .addParam(I.Goods.KEY_GOODS_ID,goodsId+"")
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void deleteCollect(Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener) {
        collect(I.REQUEST_DELETE_COLLECT,context,goodsId,userName,listener);
    }

    @Override
    public void findCollectsCount(Context context, String userName, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
