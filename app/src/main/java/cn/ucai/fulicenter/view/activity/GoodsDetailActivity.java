package cn.ucai.fulicenter.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

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
    WebView detailWebview;
    Unbinder bind;
    @BindView(R.id.detail_EnglishName)
    TextView detailEnglishName;
    @BindView(R.id.detail_name)
    TextView detailName;
    @BindView(R.id.detail_currentprice)
    TextView detailCurrentprice;

    GoodsDetailsBean mGoodsDetailsBean;
    int mGoodsId;

    IModelGoods mGoods;

    int mCurrentColo = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        bind = ButterKnife.bind(this);
        mGoodsId = getIntent().getIntExtra("GoodsId",0);
        mGoods = new ModelGoods();
        initView();
        initData();
    }

    private void initView() {
        WebSettings settings = detailWebview.getSettings();
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
                    detailCurrentprice.setText(result.getCurrencyPrice());
                    detailWebview.loadDataWithBaseURL(null,result.getGoodsBrief().trim(),I.TEXT_HTML,I.UTF_8,null);
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
        for (int i=0;i<mGoodsDetailsBean.getProperties().length;i++) {
            updateColor(i);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
