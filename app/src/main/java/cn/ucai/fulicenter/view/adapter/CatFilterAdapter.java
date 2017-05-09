package cn.ucai.fulicenter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.activity.CategoryGoodsDetailActivity;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class CatFilterAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryChildBean> Children;

    public CatFilterAdapter(Context context,
                            ArrayList<CategoryChildBean> list) {
        super();
        this.context = context;
        this.Children = list;
    }

    @Override
    public int getCount() {
        return Children == null ? 0 : Children.size();
    }

    @Override
    public CategoryChildBean getItem(int position) {
        return Children.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View layout, final ViewGroup parent) {
        ViewChildHolder holder;
        if (layout == null) {
            layout = View.inflate(context, R.layout.item_cat_filter, null);
            holder = new ViewChildHolder(layout);
            layout.setTag(holder);
        } else {
            holder = (ViewChildHolder) layout.getTag();
        }
        final CategoryChildBean child = getItem(position);
        final String name = child.getName();
        holder.tvCategoryChildName.setText(name);
        ImageLoader.downloadImg(context, holder.ivCategoryChildThumb, child.getImageUrl());
        holder.layoutCategoryChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CategoryGoodsDetailActivity.class)
                .putExtra("CategoryGoodsDetailId",child.getId())
                .putExtra("CategoryGroupName",name)
                .putExtra("CategoryChildList",Children));
            }
        });
        return layout;
    }

    class ViewChildHolder {
        @BindView(R.id.ivCategoryChildThumb)
        ImageView ivCategoryChildThumb;
        @BindView(R.id.tvCategoryChildName)
        TextView tvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout layoutCategoryChild;

        ViewChildHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
