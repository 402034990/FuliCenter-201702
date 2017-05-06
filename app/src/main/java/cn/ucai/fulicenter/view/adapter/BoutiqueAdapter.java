package cn.ucai.fulicenter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.activity.BoutiqueChildActivity;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter<BoutiqueAdapter.BoutiqueViewHolder> {
    Context context;
    ArrayList<BoutiqueBean> list;

    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BoutiqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoutiqueViewHolder(View.inflate(context, R.layout.boutique_item, null));
    }

    @Override
    public void onBindViewHolder(BoutiqueViewHolder holder, int position) {
        final BoutiqueBean boutiqueBean = list.get(position);
        holder.boutiqueName.setText(boutiqueBean.getName());
        holder.boutiqueTitle.setText(boutiqueBean.getTitle());
        holder.boutiqueDescrip.setText(boutiqueBean.getDescription());
        ImageLoader.downloadImg(context,holder.mivBoutique,boutiqueBean.getImageurl());
        holder.mivBoutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BoutiqueChildActivity.class)
                .putExtra("boutiqueChildId",boutiqueBean));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size();
    }

    public void addBoutiqueList(ArrayList<BoutiqueBean> mArrayList) {
        list.clear();
        list.addAll(mArrayList);
        notifyDataSetChanged();
    }


    static class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.miv_boutique)
        ImageView mivBoutique;
        @BindView(R.id.boutique_title)
        TextView boutiqueTitle;
        @BindView(R.id.boutique_name)
        TextView boutiqueName;
        @BindView(R.id.boutique_descrip)
        TextView boutiqueDescrip;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
