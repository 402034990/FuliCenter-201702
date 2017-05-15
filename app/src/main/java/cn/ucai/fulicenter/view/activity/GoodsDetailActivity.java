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
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        bind = ButterKnife.bind(this);
        mGoodsId = getIntent().getIntExtra("GoodsId",0);
        mGoods = new ModelGoods();
        model = new UserModel();
        initView();
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollect();
    }

    private void isCollect() {
        if (FuLiCenterApplication.getInstance().getUser() != null) {
            user = FuLiCenterApplication.getInstance().getUser();
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
        } else {
            detailCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick(R.id.detail_collect)
    public void onCollect(){
        if (FuLiCenterApplication.getInstance().getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if (isCollect) {
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

                    }
                });
            } else {
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

                    }
                });
            }
        }
    }
    private void initView() {
        WebSettings settings = detailWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }

    private void initData() {
        mGoods.loadGoodsDetailMode(this, ""+mGoodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    mGoodsDetailsBean = result;
                    detailEnglishName.setText(result.getGoodsEnglishName());
                    detailName.setText(result.getGoodsName());
                    detailCurrentPrice.setText(result.getCurrencyPrice());
                    detailWebView.loadDataWithBaseURL(null,result.getGoodsBrief().trim(),I.TEXT_HTML,I.UTF_8,null);
                    detailBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
