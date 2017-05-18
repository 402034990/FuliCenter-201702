package cn.ucai.fulicenter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.activity.GoodsDetailActivity;
import cn.ucai.fulicenter.view.activity.MainActivity;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    ArrayList<CartBean> list;
    CompoundButton.OnCheckedChangeListener changeListener;
    View.OnClickListener listener;

    public void OnListener(CompoundButton.OnCheckedChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void OnListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public CartAdapter(Context context, ArrayList<CartBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartViewHolder(View.inflate(context, R.layout.item_cart, null));
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
        final CartBean cartBean = list.get(position);
        final GoodsDetailsBean goods = cartBean.getGoods();
        holder.cartCheck.setOnCheckedChangeListener(null);
        if (goods != null) {
            ImageLoader.downloadImg(context, holder.cartIv, goods.getGoodsThumb());
            holder.cartName.setText(goods.getGoodsName());
            holder.priceCart.setText(goods.getCurrencyPrice());
            holder.countCart.setText(String.valueOf(cartBean.getCount()));
            holder.cartCheck.setChecked(cartBean.isChecked());
            holder.cartIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).startActivityForResult(new Intent(context, GoodsDetailActivity.class)
                            .putExtra("GoodsId", cartBean.getGoodsId()), 0);
                }
            });
            holder.addCart.setOnClickListener(listener);
            holder.delCart.setOnClickListener(listener);
            holder.cartCheck.setOnCheckedChangeListener(changeListener);
            holder.addCart.setTag(position);
            holder.delCart.setTag(position);
            holder.cartCheck.setTag(position);
        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void initArrayList(ArrayList<CartBean> mArrayList) {
        list.clear();
        list.addAll(mArrayList);
        notifyDataSetChanged();
    }


    static class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cartCheck)
        CheckBox cartCheck;
        @BindView(R.id.cartIv)
        ImageView cartIv;
        @BindView(R.id.cartName)
        TextView cartName;
        @BindView(R.id.addCart)
        ImageView addCart;
        @BindView(R.id.countCart)
        TextView countCart;
        @BindView(R.id.delCart)
        ImageView delCart;
        @BindView(R.id.priceCart)
        TextView priceCart;
        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
