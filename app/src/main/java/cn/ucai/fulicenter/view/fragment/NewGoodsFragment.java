package cn.ucai.fulicenter.view.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.adapter.NewGoodsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    SwipeRefreshLayout mLayout;
    RecyclerView mRecyclerView;
    TextView mTv_Hint;
    NewGoodsAdapter mAdapter;
    GridLayoutManager manager;
    static int PageId = 1;
    ArrayList<NewGoodsBean> mArrayList;

    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        initView(layout);
        downloadNewGoods(I.PAGE_ID_DEFAULT, I.ACTION_DOWNLOAD);
        setListener();
        return layout;
    }

    private void setListener() {
        mLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow)
        );
        setPullDown(I.ACTION_PULL_DOWN);
        setPullUp(I.ACTION_PULL_UP);
    }

    private void setPullUp(final int action) {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                final int lastPosition = manager.findLastVisibleItemPosition();
                mAdapter.setDragging(newState == RecyclerView.SCROLL_STATE_DRAGGING);
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore() && lastPosition >= mAdapter.getItemCount() - 1) {
                    PageId++;
                    /**
                     * 这里Footer居中有问题，未解决
                     */
                    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return position == mAdapter.getItemCount() - 1 ? manager.getSpanCount() : 1;
                        }
                    });
                    downloadNewGoods(PageId, action);

                }
            }
        });
    }

    private void setPullDown(final int action) {
        mLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageId = 1;
                mTv_Hint.setVisibility(View.VISIBLE);
                mLayout.setRefreshing(true);
                downloadNewGoods(PageId, action);
            }
        });
    }

    private void downloadNewGoods(int pageId, final int action) {
        IModelGoods goods = new ModelGoods();
        goods.loadNewGoodsMode(getContext(), 0, pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                pd.dismiss();
                mAdapter.setMore(result != null && result.length > 0);
                if (result != null) {
                    if (mAdapter.isMore()) {
                        mAdapter.setFootText(getResources().getString(R.string.load_more));
                    } else {
                        mAdapter.setFootText(getResources().getString(R.string.no_more));
                        return;
                    }
                    mArrayList = ResultUtils.array2List(result);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            mAdapter.initNewGoodsData(mArrayList);
                            break;
                        case I.ACTION_PULL_DOWN:
                            mTv_Hint.setVisibility(View.GONE);
                            mLayout.setRefreshing(false);
                            mAdapter.initNewGoodsData(mArrayList);
                            break;
                        case I.ACTION_PULL_UP:
                            mAdapter.addNewGoodsData(mArrayList);
                            break;
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pd != null) {
            pd.dismiss();
        }
    }

    private void initView(View layout) {
        mLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swip);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
        mTv_Hint = (TextView) layout.findViewById(R.id.texthint);
        mArrayList = new ArrayList<>();
        manager = new GridLayoutManager(getContext(), I.COLUM_NUM);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NewGoodsAdapter(getContext(), mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

}
