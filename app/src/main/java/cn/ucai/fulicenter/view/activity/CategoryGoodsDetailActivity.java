package cn.ucai.fulicenter.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.CatChildFilterButton;
import cn.ucai.fulicenter.view.adapter.NewGoodsAdapter;

public class CategoryGoodsDetailActivity extends AppCompatActivity {
    int CategoryId;
    @BindView(R.id.mIv_back)
    ImageView mIvBack;/*
    @BindView(R.id.mtv_Category_title)
    TextView mtvCategoryTitle;
    @BindView(R.id.category_goods_expand)
    ImageView categoryGoodsExpand;*/
    @BindView(R.id.category_detail_recyclerview)
    RecyclerView mRecyclerView;
    IModelGoods model;

    ArrayList<NewGoodsBean> mArrayList;

    NewGoodsAdapter mAdapter;

    GridLayoutManager manager;

    int PageId = I.PAGE_ID_DEFAULT;
    int PageSize = I.PAGE_SIZE_DEFAULT;
    @BindView(R.id.arrow_down)
    ImageView arrowDown;
    @BindView(R.id.arrow_up)
    ImageView arrowUp;

    boolean priceAsc = true;
    boolean addTimeAsc = false;

    String mGroupName;
    ArrayList<CategoryChildBean> arrayList;
    int sortBy;

    int imagePriceId;

    int imageTimeId;
    @BindView(R.id.catchildFilterButton)
    CatChildFilterButton catchildFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_goods_detail);
        ButterKnife.bind(this);
        CategoryId = getIntent().getIntExtra("CategoryGoodsDetailId", 0);
        mGroupName = getIntent().getStringExtra("CategoryGroupName");
        arrayList = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra("CategoryChildList");
        initView();
        initData();
        setListener();
    }

    @OnClick({R.id.arrow_down, R.id.arrow_up, R.id.mIv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_down:
                priceAsc = !priceAsc;
                sortBy = priceAsc ? I.SORT_BY_PRICE_ASC : I.SORT_BY_PRICE_DESC;
                arrowDown.setImageResource(getImagePrice());
                break;
            case R.id.arrow_up:
                addTimeAsc = !addTimeAsc;
                sortBy = addTimeAsc ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                arrowUp.setImageResource(getImageTime());
                break;
            case R.id.mIv_back:
                finish();
                break;
        }
        mAdapter.sort(sortBy);
    }


    private int getImagePrice() {
        return imagePriceId = priceAsc ? R.drawable.arrow_order_down : R.drawable.arrow_order_up;
    }

    private int getImageTime() {
        return imageTimeId = addTimeAsc ? R.drawable.arrow_order_down : R.drawable.arrow_order_up;
    }

    private void initData() {
        loadGoodsDetails(PageId, I.ACTION_DOWNLOAD);
    }

    private void setListener() {
        setPullUp();
    }

    private void setPullUp() {
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
                            return position == lastposition ? manager.getSpanCount() : 1;
                        }
                    });
                    loadGoodsDetails(PageId, I.ACTION_PULL_UP);
                }
            }
        });
    }

    private void initView() {
        mArrayList = new ArrayList<>();
        manager = new GridLayoutManager(this, I.COLUM_NUM);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NewGoodsAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        model = new ModelGoods();
        catchildFilterButton.setText(mGroupName);
        catchildFilterButton.setOnCatFilterClickListener(mGroupName,arrayList);
    }


    private void loadGoodsDetails(int pageId, final int action) {
        model.loadCategoryGoodsDetail(this, CategoryId, pageId, PageSize, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
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
                        case I.ACTION_PULL_UP:
                            mAdapter.addNewGoodsData(mArrayList);
                            break;
                    }

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
