package cn.ucai.fulicenter.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.AntiShake;
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
    @BindView(R.id.collect_hint)
    TextView collectHint;
    @BindView(R.id.Collect_swip)
    SwipeRefreshLayout collectSwipe;

    ProgressDialog pd;

    ArrayList<CollectBean> mCollectList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        bind = ButterKnife.bind(this);
        initView();
        setOnClickListener();
        iniData(pageId, I.ACTION_DOWNLOAD);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initDialog() {
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void dismiss() {
        if (pd != null) {
            pd.dismiss();
        }
    }
    private void iniData(int pageId, final int action) {
        initDialog();
        model = new UserModel();
        User user = FuLiCenterApplication.getInstance().getUser();
        model.findCollects(this, user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                dismiss();
                mAdapter.setMore(result!=null && result.length>0);
                if (result != null) {
                    mArrayList = ResultUtils.array2List(result);
                    if (mArrayList.size() < I.PAGE_SIZE_DEFAULT) {
                        center();
                    }
                    if (mAdapter.isMore()) {
                        mAdapter.setFootText(getResources().getString(R.string.load_more));
                    } else {
                        mAdapter.setFootText(getResources().getString(R.string.no_more));
                        return;
                    }
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            mCollectList.clear();
                            mCollectList.addAll(mArrayList);
                            mAdapter.initArrayList(mArrayList);
                            break;
                        case I.ACTION_PULL_UP:
                            mCollectList.addAll(mArrayList);
                            mAdapter.notifyDataSetChanged();
                            break;
                        case I.ACTION_PULL_DOWN:
                            mCollectList.clear();
                            mCollectList.addAll(mArrayList);
                            collectHint.setVisibility(View.GONE);
                            collectSwipe.setRefreshing(false);
                            mAdapter.initArrayList(mArrayList);
                            break;
                    }
                }
            }

            @Override
            public void onError(String error) {
                dismiss();
            }
        });
    }

    private void setOnClickListener() {
        OnSwipeRefreshView();
        OnScrollerView();
    }

    private void OnSwipeRefreshView() {
        collectSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId = 1;
                collectHint.setVisibility(View.VISIBLE);
                collectSwipe.setRefreshing(true);
                iniData(pageId, I.ACTION_PULL_DOWN);
            }
        });
    }

    private void OnScrollerView() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                final int lastPosition = manager.findLastVisibleItemPosition();
                mAdapter.setDragging(newState == RecyclerView.SCROLL_STATE_DRAGGING);
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() - 1 >= lastPosition&&mAdapter.isMore()) {
                    pageId++;
                    center();
                    iniData(pageId, I.ACTION_PULL_UP);
                }
            }
        });
    }

    private void center() {
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == mAdapter.getItemCount() - 1 ? manager.getSpanCount() : 1;
            }
        });
    }

    private void initView() {
        manager = new GridLayoutManager(this, I.COLUM_NUM);
        mCollectList = new ArrayList<>();
        mAdapter = new CollectAdapter(this, mCollectList);
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
        dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_COLLECT && resultCode == RESULT_OK) {
            int goodsId = data.getIntExtra("goodsid", 0);
            boolean isCollect = data.getBooleanExtra("isCollect", false);
            if (!isCollect) {
                mCollectList.remove(new CollectBean(goodsId));
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
