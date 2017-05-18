package cn.ucai.fulicenter.view.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.activity.LoginActivity;
import cn.ucai.fulicenter.view.activity.OrderActivity;
import cn.ucai.fulicenter.view.adapter.CartAdapter;

public class CartFragment extends Fragment {


    @BindView(R.id.recyclerview_cart)
    RecyclerView recyclerviewCart;
    @BindView(R.id.cart_heji)
    TextView cartHeji;
    @BindView(R.id.cart_jiesheng)
    TextView cartJiesheng;
    @BindView(R.id.buy_cart)
    Button buyCart;
    Unbinder unbinder;
    ArrayList<CartBean> mArrayList;
    CartAdapter mAdapter;
    LinearLayoutManager manager;
    IUserModel model;
    ProgressDialog pd;
    @BindView(R.id.tvRefreshHint_cart)
    TextView tvRefreshHintCart;
    @BindView(R.id.srl_cart)
    SwipeRefreshLayout srlCart;
    @BindView(R.id.mTvNothing)
    TextView mTvNothing;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    User user;
    int CurrentPrice;
    int diff;
    public CartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(I.ACTION_DOWNLOAD);
    }

    private void initBalance() {

        CurrentPrice = 0;
        int RankPrice = 0;
        diff = 0;
        if (mArrayList.size() > 0) {
            for (CartBean cartBean : mArrayList) {
                if (cartBean.isChecked()) {
                    int currentIndex = isNumeric(cartBean.getGoods().getCurrencyPrice());
                    int diffIndex = isNumeric(cartBean.getGoods().getShopPrice());
                    CurrentPrice += Integer.parseInt(cartBean.getGoods().getCurrencyPrice().substring(currentIndex)) * cartBean.getCount();
                    RankPrice += Integer.parseInt(cartBean.getGoods().getRankPrice().substring(diffIndex)) * cartBean.getCount();
                    diff = CurrentPrice - RankPrice;
                }
            }
        } else {
            CurrentPrice = 0;
            diff = 0;
        }
        String Symbol = "合计:￥ " + String.valueOf(CurrentPrice);
        String econ = "节省:￥ " + String.valueOf(diff);
        cartHeji.setText(Symbol);
        cartJiesheng.setText(econ);

    }

    private void setListener() {
        srlCart.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvRefreshHintCart.setVisibility(View.VISIBLE);
                srlCart.setRefreshing(true);
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData(final int action) {
        user = FuLiCenterApplication.getInstance().getUser();
        if (user != null) {
            initDialog();
            model.findCarts(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    if (result != null && result.length>0) {
                        llCart.setVisibility(View.VISIBLE);
                        mTvNothing.setVisibility(View.GONE);
                        mArrayList = ResultUtils.array2List(result);
                        switch (action) {
                            case I.ACTION_DOWNLOAD:
                                mAdapter.initArrayList(mArrayList);
                                break;
                            case I.ACTION_PULL_DOWN:
                                tvRefreshHintCart.setVisibility(View.GONE);
                                srlCart.setRefreshing(false);
                                mAdapter.initArrayList(mArrayList);
                                break;
                        }
                        initBalance();
                    } else {
                        llCart.setVisibility(View.GONE);
                        mTvNothing.setVisibility(View.VISIBLE);
                        mTvNothing.setText("购物车空空如也");
                    }
                    dismiss();
                }

                @Override
                public void onError(String error) {
                    dismiss();
                    llCart.setVisibility(View.GONE);
                    mTvNothing.setVisibility(View.VISIBLE);
                    mTvNothing.setText("购物车空空如也");
                }
            });
        } else {
            dismiss();
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }

    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void dismiss() {
        if (pd != null) {
            pd.dismiss();
        }
    }

    CompoundButton.OnCheckedChangeListener cbListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            update(position,isChecked);
            mArrayList.get(position).setChecked(isChecked);
            initBalance();
        }
    };

    private void update(int position, boolean isChecked) {
        CartBean cartBean = mArrayList.get(position);
        model.updateCart(getContext(), cartBean.getId(), cartBean.getCount(), isChecked, new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    initBalance();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (v.getId()) {
                case R.id.addCart:
                    updateCart(position, I.ACTION_ADD_CART);
                    break;
                case R.id.delCart:
                    updateCart(position, I.ACTION_DELETE_CART);
                    break;
            }
        }
    };


    private void updateCart(final int position, final int count) {
        final CartBean cartBean = mArrayList.get(position);
        if (cartBean.getCount() > 1) {
            addCart(position, count, cartBean);
        } else if (count < 0) {
            removeCart(position, cartBean);
        } else {
            addCart(position, count, cartBean);
        }
    }

    private void addCart(final int position, final int count, final CartBean cartBean) {
        model.updateCart(getContext(), cartBean.getId(), cartBean.getCount() + count, cartBean.isChecked(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    mArrayList.get(position).setCount(cartBean.getCount() + count);
                    mAdapter.notifyDataSetChanged();
                    initBalance();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void removeCart(final int position, CartBean cartBean) {
        model.deleteCart(getContext(), cartBean.getId(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    mArrayList.remove(position);
                    mAdapter.initArrayList(mArrayList);
                    initBalance();
                    if (mArrayList.size() == 0) {
                        llCart.setVisibility(View.GONE);
                        mTvNothing.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.i("main", "error:" + error);
            }
        });
    }

    private void initView() {
        model = new UserModel();
        mArrayList = new ArrayList<>();
        manager = new LinearLayoutManager(getContext());
        mAdapter = new CartAdapter(getContext(), mArrayList);
        mAdapter.OnListener(cbListener);
        mAdapter.OnListener(listener);
        recyclerviewCart.setAdapter(mAdapter);
        recyclerviewCart.setLayoutManager(manager);
    }

    @OnClick(R.id.buy_cart)
    public void onBuy() {
        startActivity(new Intent(getContext(), OrderActivity.class)
        .putExtra("Payment",CurrentPrice-diff));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        dismiss();
    }

    public int isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return i;
            }
        }
        return 0;
    }
}
