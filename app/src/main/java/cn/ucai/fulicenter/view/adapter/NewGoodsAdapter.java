package cn.ucai.fulicenter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.activity.GoodsDetailActivity;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class NewGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<NewGoodsBean> list;
    String FootText;
    boolean isMore;
    boolean isDragging = true;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
        notifyDataSetChanged();
    }


    public void setFootText(String footText) {
        FootText = footText;
        notifyDataSetChanged();
    }

    public NewGoodsAdapter(Context context, ArrayList<NewGoodsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View layout;
        switch (viewType) {
            case I.TYPE_ITEM:
                layout = View.inflate(context, R.layout.newgoods_item, null);
                holder = new NewGoodsViewHolder(layout);
                break;
            case I.TYPE_FOOTER:
                layout = View.inflate(context, R.layout.newgoods_footer, null);
                holder = new FooterViewHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder footer = (FooterViewHolder) holder;
            footer.mtv_footer.setText(FootText);
            return;
        }
        NewGoodsViewHolder goods = (NewGoodsViewHolder) holder;
        final NewGoodsBean goodsbean = list.get(position);
        goods.mtv_newgoods_name.setText(goodsbean.getGoodsName());
        goods.mtv_newgoods_price.setText(goodsbean.getCurrencyPrice());
        ImageLoader.downloadImg(context, goods.mivnewgoods, goodsbean.getGoodsThumb());
        goods.mivnewgoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GoodsDetailActivity.class)
                .putExtra("GoodsId",goodsbean.getGoodsId()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void sort(final int sortBy) {
        Collections.sort(list, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean o1, NewGoodsBean o2) {
                int result=0;
                switch (sortBy) {
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(o1.getCurrencyPrice())-getPrice(o2.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(o2.getCurrencyPrice())-getPrice(o1.getCurrencyPrice());
                        break;
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (o1.getAddTime() - o2.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (o2.getAddTime() - o1.getAddTime());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();
    }

    private int getPrice(String currentPrice) {
        String price = currentPrice.substring(currentPrice.indexOf("￥") + 1);
        return Integer.parseInt(price);
    }

    public void initNewGoodsData(ArrayList arrayList) {
        list.clear();
        list.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void addNewGoodsData(ArrayList arrayList) {
        list.addAll(arrayList);
        notifyDataSetChanged();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView mtv_footer;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mtv_footer = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }


    class NewGoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView mivnewgoods;
        TextView mtv_newgoods_name, mtv_newgoods_price;

        public NewGoodsViewHolder(View itemView) {
            super(itemView);
            mivnewgoods = (ImageView) itemView.findViewById(R.id.miv_newgoods);
            mtv_newgoods_name = (TextView) itemView.findViewById(R.id.newgood_name);
            mtv_newgoods_price = (TextView) itemView.findViewById(R.id.newgoods_price);
        }
    }

}
