package cn.ucai.fulicenter.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.net.IModelGoods;
import cn.ucai.fulicenter.model.net.ModelGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.adapter.BoutiqueAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {

    IModelGoods mModel;
    @BindView(R.id.texthint)
    TextView texthint;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swip)
    SwipeRefreshLayout swip;
    Unbinder unbinder;

    SwipeRefreshLayout msLayout;
    RecyclerView mRecyclerView;
    ArrayList<BoutiqueBean> mArrayList;
    TextView mtv_hint;
    BoutiqueAdapter mAdapter;
    public BoutiqueFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        unbinder = ButterKnife.bind(this, layout);
        mModel = new ModelGoods();
        downloadBoutiqueData(I.ACTION_DOWNLOAD);
        initView(layout);
        setListener();
        return layout;
    }

    private void setListener() {
        msLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow)
        );
        msLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msLayout.setRefreshing(true);
                mtv_hint.setVisibility(View.VISIBLE);
                downloadBoutiqueData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initView(View layout) {
        msLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swip);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
        mtv_hint = (TextView) layout.findViewById(R.id.texthint);
        mArrayList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mAdapter = new BoutiqueAdapter(getContext(), mArrayList);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void downloadBoutiqueData(final int action) {
        mModel.loadBoutiqueMode(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                if (result != null) {
                    mArrayList = ResultUtils.array2List(result);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            mAdapter.addBoutiqueList(mArrayList);
                            break;
                        case I.ACTION_PULL_DOWN:
                            mAdapter.addBoutiqueList(mArrayList);
                            msLayout.setRefreshing(false);
                            mtv_hint.setVisibility(View.GONE);
                            break;

                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
