package cn.ucai.fulicenter.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.adapter.NewGoodsAdapter;

public class BoutiqueChildActivity extends AppCompatActivity {

    @BindView(R.id.boutique_child_back)
    ImageView boutiqueChildBack;
    @BindView(R.id.boutiquechild_name)
    TextView boutiquechildName;
    @BindView(R.id.texthint)
    TextView texthint;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swip)
    SwipeRefreshLayout swip;

    IModelGoods mGoods;
    BoutiqueBean bqChildbean;
    Unbinder bind;

    NewGoodsAdapter mAdapter;
    GridLayoutManager manager;

    int PageId=1;

    ArrayList<NewGoodsBean> mArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        bind = ButterKnife.bind(this);
        initView();
        downloadNewGoods(I.PAGE_ID_DEFAULT,I.ACTION_DOWNLOAD);
        setListener();
    }

    private void initView() {
        bqChildbean = (BoutiqueBean) getIntent().getSerializableExtra("boutiqueChildId");
        mGoods = new ModelGoods();
        boutiquechildName.setText(bqChildbean.getTitle().trim());
        mArrayList = new ArrayList<>();
        manager = new GridLayoutManager(this, I.COLUM_NUM);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NewGoodsAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener() {
        swip.setColorSchemeColors(
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow)
        );
        setPullDown(I.ACTION_PULL_DOWN);
        setPullUp(I.ACTION_PULL_UP);
        setBackListener();
    }

    private void setBackListener() {
        boutiqueChildBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setPullUp(final int action) {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                final int lastposition = manager.findLastVisibleItemPosition();
                mAdapter.setDragging(newState == RecyclerView.SCROLL_STATE_DRAGGING);
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore() && lastposition >= mAdapter.getItemCount() - 1) {
                    PageId++;
                    /**
                     * 这里Footer居中有问题，未解决
                     */
                    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return position==lastposition? manager.getSpanCount():1;
                        }
                    });
                    downloadNewGoods(PageId,action);
                }
            }
        });
    }

    private void setPullDown(final int action) {
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageId = 1;
                texthint.setVisibility(View.VISIBLE);
                swip.setRefreshing(true);
                downloadNewGoods(PageId,action);
            }
        });
    }

    private void downloadNewGoods(int pageId,final int action) {
        IModelGoods goods = new ModelGoods();
        goods.loadNewGoodsMode(this, bqChildbean.getId(), pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                mAdapter.setMore(result!=null&&result.length>0);
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
                        texthint.setVisibility(View.GONE);
                        swip.setRefreshing(false);
                        mAdapter.initNewGoodsData(mArrayList);
                        break;
                    case I.ACTION_PULL_UP:
                        mAdapter.addNewGoodsData(mArrayList);
                        break;
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
