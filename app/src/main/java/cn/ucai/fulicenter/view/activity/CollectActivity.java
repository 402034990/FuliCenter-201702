package cn.ucai.fulicenter.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.adapter.CollectAdapter;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class CollectActivity extends AppCompatActivity {

    Unbinder bind;
    @BindView(R.id.mCollectIv_back)
    ImageView mCollectIvBack;
    @BindView(R.id.collect_recyclerview)
    RecyclerView mRecyclerView;
    CollectAdapter mAdapter;
    GridLayoutManager manager;
    ArrayList<CollectBean> mArrayList;
    IUserModel model;
    int pageId = I.PAGE_ID_DEFAULT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        bind = ButterKnife.bind(this);
        initView();
        iniData(pageId,I.ACTION_DOWNLOAD);
        setOnClickListener();
    }

    private void iniData(int pageId, final int action) {
        model = new UserModel();
        User user = FuLiCenterApplication.getInstance().getUser();
        model.findCollects(this,user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                if (result != null) {
                    mArrayList = ResultUtils.array2List(result);
                    Log.i("main", "CollectActivity.mArrayList:" + mArrayList);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            mAdapter.initArrayList(mArrayList);
                            break;
                        case I.ACTION_PULL_UP:
                            mAdapter.addArrayList(mArrayList);
                            break;
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void setOnClickListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastPosition = manager.findLastVisibleItemPosition();
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() - 1 >= lastPosition) {
                    pageId++;
                    iniData(pageId,I.ACTION_PULL_UP);
                }
            }
        });
    }

    private void initView() {
        manager = new GridLayoutManager(this, I.COLUM_NUM);
        mArrayList = new ArrayList<>();
        mAdapter = new CollectAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

    @OnClick(R.id.mCollectIv_back)
    public void onCollectBack() {
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
