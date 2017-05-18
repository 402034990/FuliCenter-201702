package cn.ucai.fulicenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.AntiShake;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.custom_view.FlowIndicator;
import cn.ucai.fulicenter.view.custom_view.SlideAutoLoopView;

public class GoodsDetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_back)
    ImageView detailBack;
    @BindView(R.id.detail_cart)
    ImageView detailCart;
    @BindView(R.id.detail_collect)
    ImageView detailCollect;
    @BindView(R.id.detail_share)
    ImageView detailShare;
    @BindView(R.id.detail_salv)
    SlideAutoLoopView mSlideAutoLoopView;
    @BindView(R.id.detaild_flowindicator)
    FlowIndicator mFlowIndicator;
    @BindView(R.id.detail_webview)
    WebView detailWebView;
    Unbinder bind;
    @BindView(R.id.detail_EnglishName)
    TextView detailEnglishName;
    @BindView(R.id.detail_name)
    TextView detailName;
    @BindView(R.id.detail_currentprice)
    TextView detailCurrentPrice;

    GoodsDetailsBean mGoodsDetailsBean;
    int mGoodsId;

    IModelGoods mGoods;
    IUserModel model;
    User user;
    boolean isCollect = false;

    AntiShake util = new AntiShake();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        bind = ButterKnife.bind(this);
        mGoodsId = getIntent().getIntExtra("GoodsId", 0);
        mGoods = new ModelGoods();
        model = new UserModel();
        initView();
        initData();
        ShareSDK.initSDK(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isCollect();
    }

    private void isCollect() {
        if (FuLiCenterApplication.getInstance().getUser() != null) {
            user = FuLiCenterApplication.getInstance().getUser();
            isCollects();
        } else {
            detailCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    private void isCollects() {
        model.isCollect(this, mGoodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null) {
                    if (result.isSuccess()) {
                        detailCollect.setImageResource(R.mipmap.bg_collect_out);
                        isCollect = true;
                    } else {
                        detailCollect.setImageResource(R.mipmap.bg_collect_in);
                        isCollect = false;
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick({R.id.detail_collect, R.id.detail_cart})
    public void onCollect(View v) {
        if (util.check(v.getId())) return;
        if (FuLiCenterApplication.getInstance().getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            switch (v.getId()) {
                case R.id.detail_collect:
                    if (isCollect) {
                        addCollect();
                    } else {
                        deleteCollect();
                    }
                    break;
                case R.id.detail_cart:
                    addCart();
                    break;
            }
        }
    }


    private void addCart() {
        model.addCarts(this, mGoodsId, user.getMuserName(), 1, false, new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    CommonUtils.showLongToast(R.string.add_cart_success);
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.add_cart_fail);
            }
        });
    }

    private void deleteCollect() {
        model.addCollect(this, mGoodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null) {
                    if (result.isSuccess()) {
                        detailCollect.setImageResource(R.mipmap.bg_collect_out);
                        CommonUtils.showLongToast(R.string.add_collect_success);
                        isCollect = true;
                    } else {
                        CommonUtils.showLongToast(R.string.add_collect_fail);
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
            }
        });
    }

    private void addCollect() {
        model.deleteCollect(this, mGoodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null) {
                    if (result.isSuccess()) {
                        detailCollect.setImageResource(R.mipmap.bg_collect_in);
                        CommonUtils.showLongToast(R.string.delete_collect_success);
                        isCollect = false;
                    } else {
                        CommonUtils.showLongToast(R.string.delete_collect_fail);
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
            }
        });
    }

    private void initView() {
        WebSettings settings = detailWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }

    private void initData() {
        mGoods.loadGoodsDetailMode(this, "" + mGoodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    mGoodsDetailsBean = result;
                    detailEnglishName.setText(result.getGoodsEnglishName());
                    detailName.setText(result.getGoodsName());
                    detailCurrentPrice.setText(result.getCurrencyPrice());
                    detailWebView.loadDataWithBaseURL(null, result.getGoodsBrief().trim(), I.TEXT_HTML, I.UTF_8, null);
                    detailBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setResult(RESULT_OK, new Intent().
                                    putExtra("goodsid", mGoodsDetailsBean.getGoodsId())
                                    .putExtra("isCollect", isCollect));
                            finish();
                        }
                    });
                    initImage();

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().
                putExtra("goodsid", mGoodsDetailsBean.getGoodsId())
                .putExtra("isCollect", isCollect));
        super.onBackPressed();
    }

    private void initImage() {
        updateColor(0);
    }

    private void updateColor(int i) {
        AlbumsBean[] album = mGoodsDetailsBean.getProperties()[i].getAlbums();
        String[] albumsImgUrl = new String[album.length];
        for (int j = 0; j < albumsImgUrl.length; j++) {
            albumsImgUrl[j] = album[j].getImgUrl();
        }
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator, albumsImgUrl, albumsImgUrl.length);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSlideAutoLoopView != null) {
            mSlideAutoLoopView.stopPlayLoop();
        }
    }

    @OnClick(R.id.detail_share)
    public void OnShare() {
        showShare();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(mGoodsDetailsBean.getGoodsName());
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(mGoodsDetailsBean.getShareUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mGoodsDetailsBean.getGoodsBrief());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(mGoodsDetailsBean.getGoodsThumb());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mGoodsDetailsBean.getShareUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(null);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(mGoodsDetailsBean.getGoodsName());
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mGoodsDetailsBean.getShareUrl());

// 启动分享GUI
        oks.show(this);
    }
}
