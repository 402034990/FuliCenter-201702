package cn.ucai.fulicenter.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

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

    public String getFootText() {
        return FootText;
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
        NewGoodsBean goodsbean = list.get(position);
        goods.mtv_newgoods_name.setText(goodsbean.getGoodsName());
        goods.mtv_newgoods_price.setText(goodsbean.getCurrencyPrice());
        ImageLoader.downloadImg(context, goods.mivnewgoods, goodsbean.getGoodsThumb(), isDragging);
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
